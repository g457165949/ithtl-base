package com.ven.controller;

import com.ven.domain.permission.Log;
import com.ven.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;


@Controller
@RequestMapping("log")
public class LogController extends BaseController{

    @Resource
    private LogService logService;

    @RequestMapping({"/", "/index"})
    public String Index() {
        return "log/index";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<Object,Object> list(Log log){
        Page<Log> logPage = logService.findAll(log,pageable());
        return success(logPage);
    }
}
