package org.ares.app.demo.actions;

import static org.ares.app.demo.common.cfg.Params.ACCESS_DENY_URL;
import static org.ares.app.demo.common.cfg.Params.ROLE_ADMIN_LABEL;
import static org.ares.app.demo.common.cfg.Params.ROLE_AUSER_LABEL;
import static org.ares.app.demo.common.cfg.Params.ROLE_NUSER_LABEL;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.ares.app.demo.daos.ParamDao;
import org.ares.app.demo.daos.UserDao;
import org.ares.app.demo.entities.SParam;
import org.ares.app.demo.entities.SUser;
import org.ares.app.demo.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BizSysAction {

	@RequestMapping(ACCESS_DENY_URL)
	public @ResponseBody String deny(){
		return "sorry,invalid user";
	}
	
	@RequestMapping("/userlogin")
	public String login(){
		return "pages/login";
	}
	
	@RequestMapping({"/","/index"})
	public String index(Model m){
		m.addAttribute("curUser", curLoginUser.getUsername());
		return "pages/index";
	}
	
	@RequestMapping("/userauth")
	public String userauth(@RequestParam(value = "curpage", defaultValue = "0") Integer curpage,
	        @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize,Model m){
		Map<String,String> roles=new HashMap<>();
		roles.put("admin", ROLE_ADMIN_LABEL);
		roles.put("adv_user", ROLE_AUSER_LABEL);
		roles.put("nor_user", ROLE_NUSER_LABEL);
		Pageable page=new PageRequest(curpage,pagesize);
		m.addAttribute("users", userdao.queryAll(page));
		m.addAttribute("roles", roles);
		m.addAttribute("curUser", curLoginUser.getUsername());
		return "pages/userauth";
	}
	
	@RequestMapping("/user/query_all")
	public String user_query(Model m){
		m.addAttribute("users", userdao.findAll());
		return "pages/userauth";
	}
	
	@RequestMapping("/user/set_adv")
	public String user_setadv(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.setAdvUser(username);
		return "redirect:/userauth";
	}
	
	@RequestMapping("/user/set_nor")
	public String user_setnor(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.setNorUser(username);
		return "redirect:/userauth";
	}
	
	@RequestMapping("/user/disable")
	public String user_dis(UserModel m){
		String username=m.getUsername();
		if(!StringUtils.isEmpty(username))
			userdao.disableUser(username);
		return "redirect:/userauth";
	}
	
	@RequestMapping("/param/setip")
	public String prm_setip(@NotNull String sand_ip){
		SParam p=paramdao.findOne("sand_ip");
		p.setVal(sand_ip);
		paramdao.save(p);
		return "redirect:/";
	}
	
	final static Logger log = LoggerFactory.getLogger(BizSysAction.class);
	@Resource UserDao userdao;
	@Resource ParamDao paramdao;
	@Resource SUser curLoginUser;
}
