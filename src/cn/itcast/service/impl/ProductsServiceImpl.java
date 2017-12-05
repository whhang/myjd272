package cn.itcast.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.dao.ProductsDao;
import cn.itcast.pojo.Page;
import cn.itcast.service.ProductsService;
@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	private ProductsDao productsDao;

	@Override
	public Page queryPageWithCondition(String queryString, String catalog_name,
			String price, Integer page, Integer rows, String sort) {
		SolrQuery query = new SolrQuery();
		if (queryString != null && !"".equals(queryString)) {
			query.setQuery(queryString);
		}else{
			query.setQuery("*:*");
		}
		//添加过滤条件
		if (catalog_name != null && !"".equals(catalog_name)) {
			query.addFilterQuery("product_catalog_name:"+catalog_name);
		}
		//过滤价格
		if (price != null && !"".equals(price)) {
			String[] split = price.split("-");
			query.addFilterQuery("product_price:["+split[0]+" TO "+split[1]+"]");
		}
		//分页设置
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认域
		query.set("df", "product_keywords");
		
		//设置排序
		if ("1".equals(sort)) {
			query.setSort("product_price", ORDER.asc);
		}else{
			query.setSort("product_price",ORDER.desc);
		}
		
		//高亮设置
		//开启高亮
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		
		//调用dao层进行查询
		Page pages = productsDao.queryProductsWithCondition(query);
		//当前页
		pages.setCurPage(page);
		//每页显示个数
		pages.setRows(rows);
		//总页数
		Integer totalRecord = pages.getTotalRecord();
		int total = totalRecord/rows;
		if (totalRecord%rows!=0) {
			total++;
		}
		pages.setTotalPages(total);
		
		return pages;
	}
	
	
}
