package cn.itcast.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.itcast.dao.ProductsDao;
import cn.itcast.pojo.Page;
import cn.itcast.pojo.Product;
@Repository
public class ProductsDaoImpl implements ProductsDao {

	@Autowired
	private HttpSolrServer solrServer;
	
	public Page queryProductsWithCondition(SolrQuery query){
		Page page = new Page();
		try {
			QueryResponse response = solrServer.query(query);
			SolrDocumentList results = response.getResults();
			//查询的总命中数
			Long numFound = results.getNumFound();
			//总命中数就是页面上展示的总记录数
			page.setTotalRecord(numFound.intValue());
			
			List<Product> pList = new ArrayList<Product>();
			for (SolrDocument doc : results) {
				Product product = new Product();
				//商品的ID
				String id = (String)doc.get("id");
				product.setPid(id);
				//商品的名称
				String pName = (String) doc.get("product_name");
				//获取高亮数据
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				Map<String, List<java.lang.String>> map = highlighting.get(id);
				List<String> list = map.get("product_name");
				if (list!=null && list.size()>0) {
					pName = list.get(0);
				}
				product.setName(pName);
				//商品价格
				Float price = (Float) doc.get("product_price");
				product.setPrice(price);
				//商品图片
				String picture = (String) doc.get("product_picture");
				product.setPicture(picture);
				pList.add(product);
			}
			page.setpList(pList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return page;
	}
}
