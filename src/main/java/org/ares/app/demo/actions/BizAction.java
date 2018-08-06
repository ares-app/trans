package org.ares.app.demo.actions;

import javax.annotation.Resource;

import org.ares.app.demo.services.SimuSandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BizAction {
	
	@RequestMapping("/query_station_info")
	public String query_station_info(Model m){
		m.addAttribute("data_list",service.query_station_info(4, 5));
		m.addAttribute("column_list", service.get_station_info_head());
		m.addAttribute("list_title", "站台信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_bus_capacity")
	public String query_bus_capacity(Model m){
		m.addAttribute("data_list",service.query_bus_capacity(15));
		m.addAttribute("column_list", service.get_bus_capacity_head());
		m.addAttribute("list_title", "车载容量信息");
		return "pages/datainfo";
	}
	
	//红绿灯系统
	@RequestMapping("/query_trafficlight_info")
	public String query_trafficlight_info(Model m){
		m.addAttribute("data_list",service.query_trafficlight_info());
		m.addAttribute("column_list", service.get_trafficlight_info_head());
		m.addAttribute("list_title", "红绿灯信息");
		return "pages/datainfo";
	}
	
	//路灯系统 
	@RequestMapping("/query_roadlight_info")
	public String query_roadlight_info(Model m){
		m.addAttribute("data_list",service.query_roadlight_info());
		m.addAttribute("column_list", service.get_roadlight_info_head());
		m.addAttribute("list_title", "路灯信息");
		return "pages/datainfo";
	}
	
	//所有传感器 
	@RequestMapping("/query_all_sense_info")
	public String query_all_sense_info(Model m){
		m.addAttribute("data_list",service.query_all_sense_info());
		m.addAttribute("column_list", service.get_all_sense_info_head());
		m.addAttribute("list_title", "传感器信息");
		return "pages/datainfo";
	}
	
	//所有道路 
	@RequestMapping("/query_road_info")
	public String query_road_info(Model m){
		m.addAttribute("data_list",service.query_road_info(20));
		m.addAttribute("column_list", service.get_road_info_head());
		m.addAttribute("list_title", "道路拥挤状态");
		return "pages/datainfo";
	}
	
	//气象信息 
	@RequestMapping("/query_weather_info")
	public String query_weather_info(Model m){
		m.addAttribute("data_list",service.query_weather_info(10));
		m.addAttribute("column_list", service.get_weather_info_head());
		m.addAttribute("list_title", "气象信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_car_info")
	public String query_car_info(Model m){
		m.addAttribute("data_list",service.query_car_info());
		m.addAttribute("column_list", service.get_car_info_head());
		m.addAttribute("list_title", "车辆基本信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_sand_car_info")
	public String query_sand_car_info(Model m){
		m.addAttribute("data_list",service.query_sand_car_info());
		m.addAttribute("column_list", service.get_sand_car_info_head());
		m.addAttribute("list_title", "沙盘车辆信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_car_account_record")
	public String query_car_account_record(Model m){
		m.addAttribute("data_list", service.query_car_account_record());
		m.addAttribute("column_list", service.get_car_account_record_head());
		m.addAttribute("list_title", "车辆充值信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_car_account_fee")
	public String query_car_account_fee(Model m){
		m.addAttribute("data_list", service.query_car_account_fee());
		m.addAttribute("column_list", service.get_car_account_fee_head());
		m.addAttribute("list_title", "车辆扣费信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_car_peccancy")
	public String query_car_peccancy(Model m){
		m.addAttribute("data_list", service.query_car_peccancy());
		m.addAttribute("column_list", service.get_car_peccancy_head());
		m.addAttribute("list_title", "车辆违章信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_peccancy_type")
	public String query_peccancy_type(Model m){
		m.addAttribute("data_list", service.query_peccancy_type());
		m.addAttribute("column_list", service.get_peccancy_type_head());
		m.addAttribute("list_title", "违章代码信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_car_user_info")
	public String query_user_info(Model m){
		m.addAttribute("data_list", service.query_car_user_info());
		m.addAttribute("column_list", service.get_car_user_info_head());
		m.addAttribute("list_title", "用户信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_etc_traffic_log")
	public String query_etc_traffic_log(Model m){
		m.addAttribute("data_list", service.getEtcTtrafficLog());
		m.addAttribute("column_list", service.get_etc_traffic_log_head());
		m.addAttribute("list_title", "ETC通行信息");
		return "pages/datainfo";
	}
	
	@RequestMapping("/query_etc_blacklist")
	public String query_blacklist(Model m){
		m.addAttribute("data_list", service.query_etc_blacklist());
		m.addAttribute("column_list", service.get_etc_blacklist_head());
		m.addAttribute("list_title", "ETC黑名单信息");
		return "pages/datainfo";
	}
	
	final static Logger log = LoggerFactory.getLogger(BizAction.class);
	@Resource SimuSandService service;
}
