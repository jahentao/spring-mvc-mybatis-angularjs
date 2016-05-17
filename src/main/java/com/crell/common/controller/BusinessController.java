package com.crell.common.controller;

import com.crell.common.model.Business;
import com.crell.common.model.User;
import com.crell.common.service.BusinessSer;
import com.crell.core.annotation.NotNull;
import com.crell.core.constant.Context;
import com.crell.core.constant.ResponseState;
import com.crell.core.controller.AbstractController;
import com.crell.core.dto.Page;
import com.crell.core.dto.ParamsBody;
import com.crell.core.dto.ReturnBody;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by crell on 2015/12/18.
 */
@RestController
public class BusinessController extends AbstractController {

    @Autowired
    BusinessSer businessSer;

    @RequestMapping(value = {"/business/list"},method = RequestMethod.POST)
    @ResponseBody
    public ReturnBody getBusinessList(@RequestBody ParamsBody paramsBody,HttpServletRequest request){
        String headerToken = request.getHeader("token");

        ReturnBody rbody = new ReturnBody();
        Page page = businessSer.getBusinessList(paramsBody.getBody(), paramsBody.getPage());
        List<Business> businessList = page.getResults();

        rbody.setStatus(ResponseState.SUCCESS);
        rbody.setData(businessList);
        rbody.setPages(page.getTotalPage());

        return rbody;
    }

    @RequestMapping(value = {"/business/{businessId}/list"},method = RequestMethod.POST)
    @ResponseBody
    public ReturnBody getBusinessById(@RequestBody ParamsBody paramsBody,@PathVariable("businessId") String businessId,HttpServletRequest request){
        ReturnBody rbody = new ReturnBody();
        Business business = businessSer.getBusinessById(businessId);

        rbody.setStatus(ResponseState.SUCCESS);
        rbody.setData(business);

        return rbody;
    }

    @RequestMapping(value = {"/business"},method = RequestMethod.POST)
    @NotNull(value = "gameName",user = true)
    @ResponseBody
    public ReturnBody add(@RequestBody ParamsBody paramsBody,HttpServletRequest request) throws Exception {
        ReturnBody rbody = new ReturnBody();
        Map<String, Object> body = paramsBody.getBody();
        User user = (User)request.getSession().getAttribute(Context.USER);

        Business business = new Business();
        BeanUtils.populate(business,body);
        business.setCreateDate(new Date());
        business.setCreator(user.getUserId());
        businessSer.add(business);

        rbody.setStatus(ResponseState.SUCCESS);
        rbody.setData(business);
        return rbody;
    }
}
