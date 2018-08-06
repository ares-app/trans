package org.ares.app.demo.services;

import static org.ares.app.demo.common.cfg.Params.ERR_MSG_MONEY_LE_ZERO;
import static org.ares.app.demo.common.cfg.Params.ERR_MSG_MONEY_NOT_SUFF;
import static org.ares.app.demo.common.cfg.Params.ERR_MSG_NOT_FOUND_ENTITY;
import static org.ares.app.demo.common.cfg.Params.ERR_MSG_PARAM_VALUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ares.app.demo.daos.AuthService;
import org.ares.app.demo.daos.CarDao;
import org.ares.app.demo.daos.CaruserDao;
import org.ares.app.demo.daos.LoginDao;
import org.ares.app.demo.daos.ParamDao;
import org.ares.app.demo.daos.PeccancyDao;
import org.ares.app.demo.daos.PectypeDao;
import org.ares.app.demo.daos.UserDao;
import org.ares.app.demo.daos.sand.SandBusStationDao;
import org.ares.app.demo.daos.sand.SandCarBlackListDao;
import org.ares.app.demo.daos.sand.SandCarChangeDao;
import org.ares.app.demo.daos.sand.SandCarFeeDao;
import org.ares.app.demo.daos.sand.SandCarInfoDao;
import org.ares.app.demo.daos.sand.SandEtcTraLogDao;
import org.ares.app.demo.daos.sand.SandMonthTemperDao;
import org.ares.app.demo.daos.sand.SandOtherSingleDao;
import org.ares.app.demo.daos.sand.SandRoadLightDao;
import org.ares.app.demo.daos.sand.SandTransLightDao;
import org.ares.app.demo.entities.Carinfo;
import org.ares.app.demo.entities.Caruser;
import org.ares.app.demo.entities.Peccancy;
import org.ares.app.demo.entities.Peccancytype;
import org.ares.app.demo.entities.SUser;
import org.ares.app.demo.entities.sand.Scarblacklist;
import org.ares.app.demo.entities.sand.Scarcharge;
import org.ares.app.demo.entities.sand.Scarfee;
import org.ares.app.demo.entities.sand.Scarinfo;
import org.ares.app.demo.entities.sand.Setctralog;
import org.ares.app.demo.entities.sand.Smonthtemperature;
import org.ares.app.demo.entities.sand.Sothersingle;
import org.ares.app.demo.entities.sand.Sroadlight;
import org.ares.app.demo.entities.sand.Stralight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SimuSandService {

	public void setCarAction(Integer carid,String action) {
		boolean dataSuccess="Start".equals(action)||"Stop".equals(action);
		if(!dataSuccess) throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		Scarinfo sc=sCarInfoDao.findOne(carid);
		if(sc==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		sc.setCaraction(action);
		sCarInfoDao.save(sc);
	}
	
	public String getCarAction(Integer carid) {
		Scarinfo sc=sCarInfoDao.findOne(carid);
		if(sc==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		return sc.getCaraction();
	}
	
	public Integer getCarAccountBalance(Integer carid) {
		Scarinfo sc=sCarInfoDao.findOne(carid);
		if(sc==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		return sc.getBalance();
	}
	
	public void setCarAccountRecharge(Integer carid,Integer money) {
		Scarinfo sc=sCarInfoDao.findOne(carid);
		if(sc==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		if(money<=0) throw new RuntimeException(ERR_MSG_MONEY_LE_ZERO);
		sc.setBalance(sc.getBalance()+money.intValue());
		sCarInfoDao.save(sc);
		Scarcharge scc=new Scarcharge();
		scc.setChargetime(new Date());
		scc.setMoney(money.intValue());
		scc.setScarinfo(sc);
		sCarChargeDao.save(scc);
	}
	
	public List<Map<String,Object>> getCarAccountRecord(Integer carid){
		List<Map<String,Object>> r=new ArrayList<>();
		sCarInfoDao.getOne(carid).getScarcharges().stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("CarId", e.getScarinfo().getCarid());
			m.put("Cost", e.getMoney());
			m.put("Time", sdf_date_time.format(e.getChargetime()));
			r.add(m);
		});
		return r;
	}
	
	public void setCarAccountFee(Integer carid,Integer money) {
		Scarinfo sc=sCarInfoDao.findOne(carid);
		if(sc==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		if(money<=0) throw new RuntimeException(ERR_MSG_MONEY_LE_ZERO);
		if(sc.getBalance()<money) throw new RuntimeException(ERR_MSG_MONEY_NOT_SUFF);
		sc.setBalance(sc.getBalance()-money.intValue());
		sCarInfoDao.save(sc);
		Scarfee scc=new Scarfee();
		scc.setFeetime(new Date());
		scc.setMoney(money.intValue());
		scc.setScarinfo(sc);
		sCarFeeDao.save(scc);
	}
	
	public List<Map<String,Object>> getCarAccountFee(Integer carid){
		List<Map<String,Object>> r=new ArrayList<>();
		sCarInfoDao.getOne(carid).getScarfees().stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("CarId", e.getScarinfo().getCarid());
			m.put("Cost", e.getMoney());
			m.put("Time", sdf_date_time.format(e.getFeetime()));
			r.add(m);
		});
		return r;
	}
	
	public void setTrafficLightNowStatus(Integer traLightid,String status,Integer time) {
		boolean dataSuccess="Red".equals(status)||"Green".equals(status)||"Yellow".equals(status);
		if(!dataSuccess) throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		Stralight tl=sTransLightDao.findOne(traLightid);
		if(tl==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		tl.setStatus(status);
		tl.setTime(time);
		sTransLightDao.save(tl);
	}
	
	public Map<String,Object> getTrafficLightNowStatus(Integer traLightid){
		Map<String,Object> r=new HashMap<>();
		Stralight tl=sTransLightDao.findOne(traLightid);
		if(tl==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		r.put("Status", tl.getStatus());
		r.put("Time", tl.getTime());
		return r;
	}
	
	public void setTrafficLightConfig(Integer traLightid,int redtime,int greentime,int yellowtime) {
		boolean dataSuccess=yellowtime>=MIN_YELLOW_TIME&&yellowtime<=MAX_YELLOW_TIME;
		if(!dataSuccess) throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		Stralight tl=sTransLightDao.findOne(traLightid);
		if(tl==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		tl.setGreentime(greentime);
		tl.setRedtime(redtime);
		tl.setYellowtime(yellowtime);
		sTransLightDao.save(tl);
	}
	
	public Map<String,Object> getTrafficLightConfigAction(Integer traLightid){
		Map<String,Object> r=new HashMap<>();
		Stralight tl=sTransLightDao.findOne(traLightid);
		r.put("RedTime", tl.getRedtime());
		r.put("GreenTime", tl.getGreentime());
		r.put("YellowTime", tl.getYellowtime());
		return r;
	}
	
	public void setRoadLightStatusAction(Integer roadlightid,String action) {
		boolean dataSuccess="Close".equals(action)||"Open".equals(action);
		if(!dataSuccess) throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		Sroadlight rl=sRoadLightDao.findOne(roadlightid);
		if(rl==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		rl.setStatus(action);
		sRoadLightDao.save(rl);
	}
	
	public String getRoadLightStatus(Integer roadlightid) {
		Sroadlight rl=sRoadLightDao.findOne(roadlightid);
		if(rl==null) throw new RuntimeException(ERR_MSG_NOT_FOUND_ENTITY);
		return rl.getStatus();
	}
	
	public void setLightSenseValve(int down,int up) {
		Sothersingle d=sOtherSingleDao.findOne("Down");
		d.setValue(down+"");
		Sothersingle u=sOtherSingleDao.findOne("Up");
		u.setValue(up+"");
		sOtherSingleDao.save(d);
		sOtherSingleDao.save(u);
	}
	
	public void setRoadLightControlMode(String cmValue) {
		boolean dataSuccess="Auto".equals(cmValue)||"Manual".equals(cmValue);
		if(!dataSuccess) throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		Sothersingle cm=sOtherSingleDao.findOne("ControlMode");
		cm.setValue(cmValue);
		sOtherSingleDao.save(cm);
	}
	
	public String getRoadLightControlMode() {
		Sothersingle cm=sOtherSingleDao.findOne("ControlMode");
		return cm.getValue();
	}
	
	public Map<String,Object> getLightSenseValve(){
		Map<String,Object> r=new HashMap<>();
		Sothersingle d=sOtherSingleDao.findOne("Down");
		Sothersingle u=sOtherSingleDao.findOne("Up");
		r.put("lower", Integer.parseInt(d.getValue()));
		r.put("upper", Integer.parseInt(u.getValue()));
		return r;
	}
	
	public List<Map<String,Object>> getEtcTtrafficLog(){
		List<Map<String,Object>> r=new ArrayList<>();
		sEtcTraLogDao.findAll().stream().forEach(e->{
			Map<String,Object> m=new LinkedHashMap<>();
			m.put("carid", e.getCarid());
			m.put("intime", sdf_date_time.format(e.getIntime()));
			m.put("outtime", sdf_date_time.format(e.getOuttime()));
			m.put("money", e.getMoney());
			r.add(m);
		});
		return r;
	}
	
	public List<Map<String,Object>> getEtcBlacklist(String username){
		List<Map<String,Object>> r=new ArrayList<>();
		//try jdk8 function filter foreach
		sCarBlackListDao.findAll().stream().filter(e->username.equals(e.getUsername())).forEach(e->{
			Map<String,Object> m=new LinkedHashMap<>();
			m.put("carid", e.getCarid());
			m.put("datetime", sdf_date_time.format(e.getDatetime()));
			r.add(m);
		});
		return r;
	}
	
	public void setEtcBlacklist(List<Map<String,String>> list,String username) {
		sCarBlackListDao.deleteByUsername(username);
		for(Map<String,String> m:list) {
			Scarblacklist cbl=new Scarblacklist();
			cbl.setCarid(Integer.parseInt(m.get("carid")));
			cbl.setUsername(username);
			try {
				cbl.setDatetime(sdf_date_time.parse(m.get("datetime")));
			} catch (ParseException e) {
				throw new RuntimeException("datetime format is invalid");
			}
			sCarBlackListDao.save(cbl);
		}
	}
	
	public void setEtcRate(int rate) {
		Sothersingle fr=sOtherSingleDao.findOne("FeeRate");
		fr.setValue(rate+"");
		sOtherSingleDao.save(fr);
	}
	
	public int getEtcRate() {
		Sothersingle fr=sOtherSingleDao.findOne("FeeRate");
		return Integer.parseInt(fr.getValue());
	}
	
	public int getRoadStatus(int roadid) {
		return randomOfRange(1,5);
	}
	
	public int getBusCapacity(int busid) {
		return randomOfRange(30,100);
	}
	
	public List<Map<String,Object>> getBusStationInfoFromDB(int busstationid) {
		List<Map<String,Object>> r=new ArrayList<>();
		sBusStationDao.findByBusstationid(busstationid).stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("Distance", e.getDistance());
			m.put("BusId", e.getBusid());
			r.add(m);
		});
		return r;
	}
	
	/**
	 * 随机获取距离
	 * @param busstationid
	 * @return
	 */
	public List<Map<String,Object>> getBusStationInfo(int busstationid,int buscount) {
		List<Map<String,Object>> r=new ArrayList<>();
		for(int i=1;i<=buscount;i++){
			Map<String,Object> m=new HashMap<>();
			m.put("Distance", randomOfRange(DISTANCE[0],DISTANCE[1]));
			m.put("BusId", Integer.valueOf(i));
			r.add(m);
		}
		return r;
	}
	
	//WCurrent	WData,temperature	ROWS_DETAIL
	public Map<String,Object> getWeather(){
		Map<String,Object> r=new HashMap<>();
		r.put("WCurrent", getTemperOfToday());
		int days_length=6;
		List<Map<String,?>> rows=new ArrayList<>();
		getTemperRangeOfAfterToday(days_length).stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("WData", e[0]);
			m.put("temperature", e[1]);
			rows.add(m);
		});
		r.put("ROWS_DETAIL", rows);
		return r;
	}
	
	/**
	 * 获取给定范围随机数
	 * @param start
	 * @param end
	 */
	int randomOfRange(int start,int end){
		if (start > end){
			int tmp=end;
			end=start;
			start=tmp;
		}
		return (int)(Math.random()*(end-start+1)+start);
	}
	
	/**
	 * 获取给定月份温度范围
	 * @param month
	 */
	int[] getTempeRangeByMonth(int month) {
		if(month>12 ||month<1)
			throw new RuntimeException("Month is invalid");
		Smonthtemperature e=sMonthTemperDao.getOne(month);
		return new int[] {e.getMint(),e.getMaxt()};
	}
	
	int getTemperOfMonthDay(int month,int day) {
		if(month>12||month<1||day<1||day>31)
			throw new RuntimeException("Month or Day is invalid");
		int[] cur_month_temper=getTempeRangeByMonth(month);
		int start=cur_month_temper[0];
		start+=randomOfRange(1,5);
		int end=cur_month_temper[1];
		float adjust=day/30f;
		if(8<=month)
			adjust=(1-adjust);
		int temperOfToday=(int)(adjust*(end-start))+start;
		return temperOfToday;
	}
	
	int getTemperOfToday() {
		LocalDate now=LocalDate.now();
		int month=now.getMonthValue();
		int day=now.getDayOfMonth();
		return getTemperOfMonthDay(month,day);
	}
	
	int[] getTemperRangeOfToday() {
		int[] r=new int[2];
		int start=getTemperOfToday();
		int end=start+randomOfRange(3,8);
		r[0]=start;
		r[1]=end;
		return r;
	}
	
	List<String[]> getTemperRangeOfAfterToday(int days) {
		List<String[]> r=new ArrayList<>();
		int today_start=getTemperOfToday();
		LocalDate now=LocalDate.now();
		for(int i=0;i<days;i++) {
			String[] min_max=new String[2];
			int start=today_start+randomOfRange(-2,2);
			int end=start+randomOfRange(3,8);
			now=now.plusDays(1l);
			min_max[0]=now+"";
			min_max[1]=start+"~"+end;
			r.add(min_max);
		}
		return r;
	}
	
	//pm2.5,co2,LightIntensity,humidity,temperature
	public Map<String,Object> getAllSense() {
		Map<String,Object> r=new HashMap<>();
		r.put("pm2.5", randomOfRange(PM25[0],PM25[1]));
		r.put("co2", randomOfRange(CO2[0],CO2[1]));
		r.put("temperature", getTemperOfToday());
		r.put("humidity", randomOfRange(HUMIDITY[0],HUMIDITY[1]));
		r.put("LightIntensity", randomOfRange(LIGHTINTENSITY[0],LIGHTINTENSITY[1]));
		return r;
	}
	
	public Object getSenseByName(String key) {
		Object value=getAllSense().get(key);
		if(value==null)
			throw new RuntimeException(ERR_MSG_PARAM_VALUE);
		return value;
	}
	
	@Scheduled(cron="${etc.auto.fee.frequency}")
	public void etcAutoFee(){
		if(!auto_fee) return;
		int etcRate=getEtcRate();
		sCarInfoDao.findAll().forEach(e->{
			try{
				setCarAccountFee(e.getCarid(),etcRate);
				log.debug("["+e.getCarid()+"] fee:"+etcRate);
			}catch(Exception ex){
				
			}
		});
	}
	
	//----------------------------------------------------------------------------------------------------------
	public List<Carinfo> query_car_info(){
		return cardao.findAll();
	}
	
	public List<Map<String,String>> get_car_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carnumber","number","pcardid","carbrand","buydate"};
		String[] heads_title= {"车牌号","车辆编号","用户标识","品牌","购买日期"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String, Object>> query_car_account_record() {
		List<Map<String, Object>> r = new ArrayList<>();
		sCarInfoDao.findAll().stream().forEach(c -> {
			c.getScarcharges().stream().forEach(e -> {
				Map<String, Object> m = new HashMap<>();
				m.put("carid", e.getScarinfo().getCarid());
				m.put("cost", e.getMoney());
				m.put("time", sdf_date_time.format(e.getChargetime()));
				r.add(m);
			});
		});
		return r;
	}
	
	public List<Map<String,String>> get_car_account_record_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carid","cost","time"};
		String[] heads_title= {"车辆编号","充值金额","充值时间"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String, Object>> query_car_account_fee() {
		List<Map<String, Object>> r = new ArrayList<>();
		sCarInfoDao.findAll().stream().forEach(c -> {
			c.getScarfees().stream().forEach(e -> {
				Map<String, Object> m = new HashMap<>();
				m.put("carid", e.getScarinfo().getCarid());
				m.put("cost", e.getMoney());
				m.put("time", sdf_date_time.format(e.getFeetime()));
				r.add(m);
			});
		});
		return r;
	}
	
	public List<Map<String,String>> get_car_account_fee_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carid","cost","time"};
		String[] heads_title= {"车辆编号","扣费金额","扣费时间"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Peccancy> query_car_peccancy(){
		return peccancydao.findAll();
	}
	
	public List<Map<String,String>> get_car_peccancy_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carnumber","pcode","paddr","pdatetime"};
		String[] heads_title= {"车牌号","违章号","违章地址","违章时间"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Scarinfo> query_sand_car_info(){
		return sCarInfoDao.findAll();
	}
	
	public List<Map<String,String>> get_sand_car_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carid","caraction","balance"};
		String[] heads_title= {"车辆编号","行驶状态","余额"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	
	public List<Peccancytype> query_peccancy_type(){
		return pecdao.findAll();
	}
	
	public List<Map<String,String>> get_peccancy_type_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"pcode","pmoney","pscore","premarks"};
		String[] heads_title= {"违章代码","金额","扣除分数","代码含义"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<SUser> query_user_info(){
		return userdao.findAll();
	}
	
	public List<Setctralog> query_etc_traffic_log(){
		return sEtcTraLogDao.findAll();
	}
	
	public List<Map<String,String>> get_etc_traffic_log_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carid","intime","outtime","money"};
		String[] heads_title= {"车辆编号","驶入时间","离开时间","应付金额"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Caruser> query_car_user_info(){
		return caruserdao.findAll();
	}
	
	public List<Map<String,String>> get_car_user_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"username","pname","pcardid","psex","ptel","pregisterdate"};
		String[] heads_title= {"系统用户名称","用户姓名","身份证号","性别","电话","注册时间"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String, Object>> query_station_info(int station_count, int bus_count) {
		List<Map<String, Object>> r = new ArrayList<>();
		for (int s = 1; s <= station_count; s++) {
			for (int i = 1; i <= bus_count; i++) {
				Map<String, Object> m = new HashMap<>();
				m.put("distance", randomOfRange(DISTANCE[0], DISTANCE[1]));
				m.put("busid", Integer.valueOf(i));
				m.put("stationid", Integer.valueOf(s));
				r.add(m);
			}
		}
		return r;
	}
	
	public List<Map<String,String>> get_station_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"stationid","busid","distance"};
		String[] heads_title= {"站台编号","公交车辆编号","公交站台距离"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,Object>> query_bus_capacity(int bus_count) {
		List<Map<String,Object>> r=new ArrayList<>();
		for(int i=1;i<=bus_count;i++) {
			Map<String,Object> m=new HashMap<>();
			m.put("busid", Integer.valueOf(i));
			m.put("capacity", Integer.valueOf(randomOfRange(30,100)));
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,String>> get_bus_capacity_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"busid","capacity"};
		String[] heads_title= {"公交车辆编号","车载人数"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,Object>> query_etc_blacklist(){
		List<Map<String,Object>> r=new ArrayList<>();
		sCarBlackListDao.findAll().stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("carid", e.getCarid());
			m.put("datetime", sdf_date_time.format(e.getDatetime()));
			m.put("username", e.getUsername());
			r.add(m);
		});
		return r;
	}
	
	public List<Map<String,String>> get_etc_blacklist_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"carid","datetime","username"};
		String[] heads_title= {"黑名单车辆编号","下发时间","下发者"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,?>> query_trafficlight_info(){
		List<Map<String,?>> r=new ArrayList<>();
		sTransLightDao.findAll().stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("traflid", e.getTrafficlightid());
			m.put("redtime", e.getRedtime());
			m.put("greentime", e.getGreentime());
			m.put("yellowtime", e.getYellowtime());
			m.put("status", e.getStatus());
			r.add(m);
		});
		return r;
	}
	
	public List<Map<String,String>> get_trafficlight_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"traflid","redtime","greentime","yellowtime","status"};
		String[] heads_title= {"红绿灯编号","红灯时长","绿灯时长","黄灯时长","当前状态"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,?>> query_roadlight_info(){
		List<Map<String,?>> r=new ArrayList<>();
		sRoadLightDao.findAll().stream().forEach(e->{
			Map<String,Object> m=new HashMap<>();
			m.put("roadlgid", e.getRoadlightid());
			m.put("ctlmode", e.getControlmode());
			m.put("status", e.getStatus());
			r.add(m);
		});
		return r;
	}
	
	public List<Map<String,String>> get_roadlight_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"roadlgid","ctlmode","status"};
		String[] heads_title= {"路灯编号","模式","状态"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,?>> query_road_info(int count){
		List<Map<String,?>> r=new ArrayList<>();
		for(int i=1;i<=count;i++) {
			Map<String,Object> m=new HashMap<>();
			m.put("roadid", Integer.valueOf(i));
			m.put("status", Integer.valueOf(randomOfRange(1,5)));
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,String>> get_road_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"roadid","status"};
		String[] heads_title= {"道路编号","拥挤程度"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,?>> query_weather_info(int count){
		List<Map<String,?>> r=new ArrayList<>();
		Map<String,Object> mcur=new HashMap<>();
		mcur.put("temperature", getTemperOfToday());
		mcur.put("date", sdf_date.format(new Date()));
		r.add(mcur);
		int days_length=count;
		getTemperRangeOfAfterToday(days_length).stream().forEach(e->{
			Map<String,Object> m=new LinkedHashMap<>();
			m.put("date", e[0]);
			m.put("temperature", e[1]);
			r.add(m);
			log.debug(""+m);
		});
		return r;
	}
	
	public List<Map<String,String>> get_weather_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"date","temperature"};
		String[] heads_title= {"日期","温度"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	public List<Map<String,?>> query_all_sense_info(){
		List<Map<String,?>> r=new ArrayList<>();
		Map<String,Object> e1=new HashMap<>();
		e1.put("sensename", "pm2.5");
		e1.put("value", randomOfRange(PM25[0],PM25[1]));
		r.add(e1);
		Map<String,Object> e2=new HashMap<>();
		e2.put("sensename", "co2");
		e2.put("value", randomOfRange(CO2[0],CO2[1]));
		r.add(e2);
		Map<String,Object> e3=new HashMap<>();
		e3.put("sensename", "temperature");
		e3.put("value", getTemperOfToday());
		r.add(e3);
		Map<String,Object> e4=new HashMap<>();
		e4.put("sensename", "humidity");
		e4.put("value", randomOfRange(HUMIDITY[0],HUMIDITY[1]));
		r.add(e4);
		Map<String,Object> e5=new HashMap<>();
		e5.put("sensename", "LightIntensity");
		e5.put("value", randomOfRange(LIGHTINTENSITY[0],LIGHTINTENSITY[1]));
		r.add(e5);
		return r;
	}
	
	public List<Map<String,String>> get_all_sense_info_head(){
		List<Map<String,String>> r=new ArrayList<>();
		String[] heads_name= {"sensename","value"};
		String[] heads_title= {"传感器","测量结果"};
		for(int i=0;i<heads_name.length;i++) {
			Map<String,String> m=new LinkedHashMap<>();
			m.put("column_name", heads_name[i]);
			m.put("column_title", heads_title[i]);
			r.add(m);
		}
		return r;
	}
	
	static final int[] PM25= {0,299};
	static final int[] CO2= {15,9995};
	static final int[] LIGHTINTENSITY= {1,4092};
	static final int[] HUMIDITY= {20,90};
	static final int[] TEMPERATURE= {0,49};
	static final int[] DISTANCE= {0,110850};
	
	static final int MIN_YELLOW_TIME=3;
	static final int MAX_YELLOW_TIME=5;
	
	final Logger log = LoggerFactory.getLogger(getClass());
	final SimpleDateFormat sdf_date_time=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	final SimpleDateFormat sdf_date=new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource SandCarInfoDao sCarInfoDao;
	@Resource SandCarChangeDao sCarChargeDao;
	@Resource SandCarFeeDao sCarFeeDao;
	@Resource SandTransLightDao sTransLightDao;
	@Resource SandRoadLightDao sRoadLightDao;
	@Resource SandMonthTemperDao sMonthTemperDao;
	@Resource SandOtherSingleDao sOtherSingleDao;
	@Resource SandBusStationDao sBusStationDao;
	@Resource SandEtcTraLogDao sEtcTraLogDao;
	@Resource SandCarBlackListDao sCarBlackListDao;
	
	@Resource UserDao userdao;
	@Resource ParamDao prmdao;
	@Resource AuthService auth;
	@Resource ParamDao paramdao;
	@Resource PeccancyDao peccancydao;
	@Resource CarDao cardao;
	@Resource CaruserDao caruserdao;
	@Resource LoginDao logindao;
	@Resource PectypeDao pecdao;
	
	@Value("${etc.auto.fee.enable}")
	boolean auto_fee;
	
}
