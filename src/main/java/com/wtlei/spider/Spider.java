package com.wtlei.spider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.wtlei.entity.ConnectOutUrl;
import com.wtlei.entity.Image;
import com.wtlei.service.ConnectOutUrlService;
import com.wtlei.service.ImageService;
import com.wtlei.task.ThreadTask;
import com.wtlei.utils.BeanContext;
import com.wtlei.utils.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author wtlei
 * @name Spider
 * @date 2019-12-08 13:46
 */

@Component
@Slf4j
public class Spider {

    @Resource
    private ConnectOutUrlService connectOutUrlService;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36";

    private static final String BASE_FILE_PATH = "H:/temp/www.meitulu.com/";

    private static final int MAX_PAGE = 20335;

    private static final String BASE_URL = "https://www.meitulu.com/item/PAGE.html";

    private static int TOTAL_COUNT = 1;

    @Resource
    private ImageService imageService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 处理图片
     *
     * @param url 图片地址
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveImage(String url) throws Exception {

        log.info("准备开始爬取页面：" + url);
        //模仿cookie
        Map<String, String> cookies = new HashMap<>(4);
        cookies.put("UM_distinctid", "16ee366fb47225-0c28afac7589e6-2393f61-1fa400-16ee366fb48636");
        cookies.put("CNZZDATA1255357127", "403303289-1575769637-https%253A%252F%252Fwww.baidu.com%252F%7C1576304396");
        cookies.put("CNZZDATA1255487232", "1141914786-1575775040-%7C1576306432");

        Document document = Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .cookies(cookies)
                .timeout(5000)
                .ignoreHttpErrors(true)
                .get();

        Element c_l = document.getElementsByClass("c_l").get(0);
        Elements ps = c_l.getElementsByTag("p");
        Elements elements = document.getElementsByClass("content_img");
        //获取标签
        Element fenxiang = document.getElementsByClass("fenxiang_l").get(0);
        Elements tags = fenxiang.getElementsByClass("tags");
        StringBuilder imageTags = new StringBuilder();
        for (Element tag : tags) {
            imageTags.append("[").append(tag.text()).append("]").append(" ");
        }
        for (Element element : elements) {
            Image image = new Image();
            image.setImageTags(imageTags.toString());
            image.setId(IdWorker.get32UUID());
            String imgName = element.attr("alt");
            image.setImageName(imgName);
            String title = imgName.substring(0, imgName.lastIndexOf("第"));
            image.setTitle(title);
            String path = element.attr("src");
            image.setPath(path);
            for (Element p : ps) {
                //获取发行机构
                if (p.text().contains("发行机构")) {
                    image.setIssuer(p.text().split("：")[1]);
                }
                //获取期刊编号
                if (p.text().contains("期刊编号")) {
                    image.setPeriodicalNum(p.text().split("：")[1]);
                }
                //获取图片数量
                if (p.text().contains("图片数量")) {
                    image.setImageCount(p.text().split("：")[1]);
                }
                //获取分辨率
                if (p.text().contains("分 辨 率")) {
                    image.setResolution(p.text().split("：")[1]);
                }
                //获取模特姓名
                if (p.text().contains("模特姓名")) {
                    image.setModelName(p.text().split("：")[1]);
                }
                //获取发行时间
                if (p.text().contains("发行时间")) {
                    image.setIssueDate(p.text().split("：")[1]);
                }
            }
            //设置保存的本地路径
            String localPath = BASE_FILE_PATH + title + path.substring(path.lastIndexOf("/"));
            image.setLocalPath(localPath);
            //这里防止重复保存同一张图片
            QueryWrapper<Image> wrapper = new QueryWrapper<>();
            wrapper.eq("path", path);
            this.imageService = BeanContext.getApplicationContext().getBean(ImageService.class);
            Image sysImage = this.imageService.getOne(wrapper);
            if (Objects.isNull(sysImage)) {
                this.imageService.save(image);
                log.info(imgName + "已保存到数据库...");
            }

            //把图片保存到本地
            URLConnection conn = new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("userAgent", USER_AGENT);
            conn.setReadTimeout(3000);
            InputStream is = conn.getInputStream();
            File file = new File(localPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            FileOutputStream os = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int j = 0;
            while ((j = is.read(b)) != -1) {
                os.write(b, 0, j);
            }
            log.info(imgName + "已保存到本地...");
            log.info("总共爬取了" + (TOTAL_COUNT++) + "张图片...");
            is.close();
            os.close();
        }
    }

    /**
     * 下一页
     *
     * @param url
     * @throws Exception
     */
    public void nextPage(String url) throws Exception {
        try {
            //处理连接超时问题
            URL url2;
            url2 = new URL(url);
            URLConnection connection = url2.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();

            //这里防止资源不存在
            HttpURLConnection huc = (HttpURLConnection) url2.openConnection();
            huc.connect();
            int responseCode = huc.getResponseCode();
            if (responseCode != 200 && responseCode != 304) {
                log.warn("当前页面无效，继续爬取下一个页面...");
                return;
            }

            saveImage(url);
            Document document = Jsoup.connect(url).get();
            Element pages = document.getElementById("pages");
            Elements children = pages.children();
            String maxNum = children.get(children.size() - 2).text();
            int maxPage = Integer.parseInt(maxNum);

            for (int j = 2; j <= maxPage; j++) {
                String suffix = "_" + j + ".html";
                String replace = url.substring(url.lastIndexOf("."));
                String nexPage = url.replace(replace, suffix);
                log.info("准备爬取当前第" + j + "页");
                saveImage(nexPage);
            }
        } catch (IOException e) {
            log.error(url + "连接超时...");
            ConnectOutUrl connectOutUrl = new ConnectOutUrl();
            connectOutUrl.setId(IdWorker.get32UUID());
            connectOutUrl.setUrl(url);
            connectOutUrlService.save(connectOutUrl);
            log.info(url + "超时链接已保存到数据库...");
        }
    }

    /**
     * 开始爬虫
     */
    public void startSpider() throws Exception {

        //获取系统处理器个数作为线程池数量
        int nThreads = Runtime.getRuntime().availableProcessors();
        //创建线程池
        ExecutorService executorService = ThreadPoolFactoryUtil.getExecutorService(nThreads);
        for (int i = 178; i <= MAX_PAGE; i++) {
            String url = BASE_URL.replace("PAGE", i + "");
            ThreadTask task = new ThreadTask(url);
            executorService.execute(task);
        }
        //关闭线程池
        executorService.shutdown();
    }
}
