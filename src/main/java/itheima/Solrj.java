package itheima;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class Solrj {
    @Test
    public void simpleQuery()throws Exception{
        //指定远程服务器地址
        String url = "http://localhost:8080/solr/collection1";
        //创建服务对象,连接远程solr服务
        HttpSolrServer solrServer = new HttpSolrServer(url);
        //创建参数封装对象,所有查询参数必须封装到这个对象中查询
        SolrQuery solrQuery = new SolrQuery();
        //设置主查询条件参数即可
        solrQuery.set("q","product_name:浴巾");
        //使用solr服务查询索引库
        QueryResponse response = solrServer.query(solrQuery);

        //获取查询文档集合
        SolrDocumentList results = response.getResults();
        //获取命中总记录数
        long numFound = results.getNumFound();
        System.out.println("命中总记录数"+numFound);
        //循环文档集合
        for (SolrDocument result : results) {
            //获取id
            String id = (String) result.get("id");
            System.out.println("文档域id:"+id);
            //获取商品标题
            String product_name = (String) result.get("product_name");
            System.out.println("文档域product_name:"+product_name);
            //获取商品价格
            Float product_price = (Float) result.get("product_price");
            System.out.println("文档域product_price:"+product_price);
            //获取商品描述
            String product_description = (String) result.get("product_description");
            System.out.println("文档域product_description:"+product_description);
            //获取商品图片
            String product_picture = (String) result.get("product_picture");
            System.out.println("文档域product_picture:"+product_picture);
            //获取商品的分类
            String product_catalog_name = (String) result.get("product_catalog_name");
            System.out.println("文档域product_catalog_name:"+product_catalog_name);


        }
    }
    @Test
    public void queryIndex()throws Exception{
        //指定远程服务器索引库地址
        String baseURL="http://localhost:8080/solr/collection1";
        //连接solr远程索引库
        HttpSolrServer solrServer = new HttpSolrServer(baseURL);
        //创建封装查询条件对象
        SolrQuery solrQuery = new SolrQuery();
        //设置主查询条件
        solrQuery.setQuery("浴巾");
        //设置过滤查询条件:
        //a.查询商品类别是时尚卫浴
        solrQuery.addFilterQuery("product_catalog_name:时尚卫浴");
        //商品价格在1-20
        solrQuery.addFilterQuery("product_price:[1 TO 20]");
        // 设置排序
        //第一参数:指定哪个域进行排序
        //第二个参数:升序,降序
        solrQuery.setSort("product_price",SolrQuery.ORDER.asc);
        //分页
        solrQuery.setStart(1);
        solrQuery.setRows(6);
        //设置过滤字段
        solrQuery.setFields("id,product_name,product_price");
        //设置高亮
        //开启高亮
        solrQuery.setHighlight(true);
        //设置高亮字段
        solrQuery.addHighlightField("product_price");
        //指定显示前缀
        solrQuery.setHighlightSimplePre("<font color='red>");
        //设置后缀
        solrQuery.setHighlightSimplePost("<font>");
//        设置默认查询字段
        solrQuery.set("df","product_keywords");
        //使用solrServer执行查询
        QueryResponse response = solrServer.query(solrQuery);
        //获取结果集对象
        SolrDocumentList results = response.getResults();
        // 获取命中总记录数
        long numFound = results.getNumFound();
        System.out.println("命中总记录数：" + numFound);

        // 循环获取每一个文档对象
        for (SolrDocument sdoc : results) {

            String id = (String) sdoc.get("id");
            System.out.println("文档ID：" + id);
            String productName = (String) sdoc.get("product_name");

            // 获取高亮
            Map<String, Map<String, List<String>>> highlighting = response
                    .getHighlighting();
            //第一个map的key就是Id
            Map<String, List<String>> map = highlighting.get(id);
            //第二个map的key是域名
            List<String> list = map.get("product_name");

            if(list!=null && list.size()>0){
                productName = list.get(0);
            }

            System.out.println("商品名称：" + productName);

            Float productPrice = (Float) sdoc.get("product_price");
            System.out.println("商品价格：" + productPrice);
            String productDescription = (String) sdoc
                    .get("product_description");
            System.out.println("商品描述：" + productDescription);
            String productPicture = (String) sdoc.get("product_picture");
            System.out.println("商品图片：" + productPicture);
            String productCatalogName = (String) sdoc
                    .get("product_catalog_name");
            System.out.println("商品类别：" + productCatalogName);
            System.out.println("第一次修改");
            System.out.println("第er次修改");


        }

    }
}
