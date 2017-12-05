package cn.itcast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.itcast.pojo.Page;
import cn.itcast.service.ProductsService;

@Controller
public class ProductsController {
	
	@Autowired
	private ProductsService productsService;

	/**
	 * 
	 * @param queryString   查询的条件 
	 * @param catalog_name  商品类别名称
	 * @param price    商品的价格
	 * @param sort   商品按照什么进行排序
	 */
	@RequestMapping("list")
	public String queryProducts(String queryString,
								String catalog_name,
								String price,
								@RequestParam(defaultValue="1")Integer page,
								@RequestParam(defaultValue="60")Integer rows,
								@RequestParam(defaultValue="1")String sort,
								Model model){
		//调用service层进行查询
		Page result = productsService.queryPageWithCondition(queryString, catalog_name, price, page, rows, sort);
		
		//回显查询的条件
		model.addAttribute("queryString", queryString);
		
		model.addAttribute("catalog_name", catalog_name);
		
		model.addAttribute("price", price);
		
		model.addAttribute("sort", sort);
		
		//回显分页信息
		model.addAttribute("result",result );
		return "product_list";
	}
}
