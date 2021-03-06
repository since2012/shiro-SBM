package org.tc.shiro.modular.biz.controller;


import com.github.pagehelper.PageInfo;
import com.stylefeng.guns.core.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tc.mybatis.controller.BaseController;
import org.tc.mybatis.dto.GlobalResult;
import org.tc.mybatis.exception.GunsException;
import org.tc.mybatis.tips.Tip;
import org.tc.shiro.core.common.exception.BizExceptionEnum;
import org.tc.shiro.core.dto.Exposer;
import org.tc.shiro.core.dto.SeckillExecutionResult;
import org.tc.shiro.core.dto.SeckillTimeData;
import org.tc.shiro.core.shiroext.kit.ShiroKit;
import org.tc.shiro.core.shiroext.vo.ShiroUser;
import org.tc.shiro.modular.biz.service.IStockService;
import org.tc.shiro.po.Stock;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController {

    private String PREFIX = "biz/stock/";

    @Autowired
    private IStockService stockService;

    /**
     * 库存管理
     *
     * @return
     */
    @GetMapping("")
    public String index() {
        return PREFIX + "stock";
    }

    /**
     * 秒杀列表
     *
     * @return
     */
    @GetMapping("show")
    public String show() {
        return PREFIX + "show";
    }

    /**
     * 秒杀列表
     *
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public Object list(Stock stock,
                       @RequestParam(required = false) Integer limit,
                       @RequestParam(required = false) Integer offset,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order) {
        if (StringUtils.isBlank(sort)) {
            sort = "name asc";
        } else {
            sort = sort + " " + order;
        }
        PageInfo<Stock> page = stockService.page(stock, offset / limit + 1, limit, sort);
        return super.warpForBT(page.getList(), page.getTotal());
    }


    /**
     * 新增页
     */
    @GetMapping("/add")
    public String addView() {
        return PREFIX + "add";
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    @ResponseBody
    public Tip add(@Valid Stock stock, BindingResult result) {
        if (result.hasErrors()) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        stockService.add(stock);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{stockId}")
    public String stockEdit(@PathVariable Integer stockId, Model model) {
        if (ToolUtil.isEmpty(stockId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        Stock stock = this.stockService.selectByPK(stockId);
        model.addAttribute("stock", stock);
        return PREFIX + "edit";
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    @ResponseBody
    public Tip edit(@Valid Stock stock, BindingResult result) {
        if (result.hasErrors() || stock.getId() == null) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        stockService.edit(stock);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Tip delete(@RequestParam Long stockId) {
        if (ToolUtil.isEmpty(stockId)) {
            throw new GunsException(BizExceptionEnum.REQUEST_NULL);
        }
        this.stockService.del(stockId);
        return SUCCESS_TIP;
    }

    /**
     * 秒杀详情
     *
     * @param stockId
     * @param model
     * @return
     */
    @GetMapping("/{stockId}/seckill")
    public String seckill(@PathVariable("stockId") Long stockId, Model model) {
        if (stockId == null) {
            //重定向：丢失数据
            return REDIRECT + "/stock/list";
        }
        boolean exists = stockService.existsByPK(stockId);
        if (!exists) {
            //转发数据
            return FORWARD + "/stock/list";
        }
        Stock stock = stockService.selectByPK(stockId);
        model.addAttribute("stock", stock);
        return PREFIX + "seckill";
    }

    /**
     * 秒杀地址暴露
     *
     * @param stockId
     * @return
     */
    @PostMapping("/{stockId}/exposer")
    @ResponseBody
    public Object exposer(@PathVariable("stockId") Long stockId) {
        GlobalResult<Exposer> result;
        try {
            Exposer exposer = stockService.exoportSeckillUrl(stockId);
            result = GlobalResult.ok(exposer);
        } catch (Exception e) {
            result = GlobalResult.errorMsg(e.getMessage());
        }
        return result;
    }


    @PostMapping("/{stockId}/{md5}/execute")
    @ResponseBody
    public Object exec(@PathVariable("stockId") Long stockId,
                       @PathVariable("md5") String md5) {
        //未登录
        ShiroUser shiroUser = ShiroKit.getUser();
        if (ToolUtil.isEmpty(shiroUser)) {
            return GlobalResult.errorMsg("你还未登录");
        }
        SeckillExecutionResult seckillExecutionResult = null;
        seckillExecutionResult = stockService.executeSeckill(stockId, md5);
        return GlobalResult.ok(seckillExecutionResult);
    }

    /**
     * 执行秒杀(采用存储过程)
     *
     * @param stockId
     * @param md5
     * @return
     */
    @PostMapping("/{stockId}/{md5}/execution")
    @ResponseBody
    public Object execute(@PathVariable("stockId") Long stockId,
                          @PathVariable("md5") String md5) {
        //未登录
        ShiroUser shiroUser = ShiroKit.getUser();
        if (ToolUtil.isEmpty(shiroUser)) {
            return GlobalResult.errorMsg("你还未登录");
        }
        SeckillExecutionResult execution = stockService.executeSeckillProcedure(stockId, md5);
        return GlobalResult.ok(execution);
    }

    @GetMapping("/{stockId}/time")
    @ResponseBody
    public Object now(@PathVariable("stockId") Long stockId) {
        Date date = new Date();
        Stock stock = stockService.selectByPK(stockId);
        SeckillTimeData timeData = new SeckillTimeData(date.getTime(), stock.getBegintime().getTime(), stock.getEndtime().getTime());
        return GlobalResult.ok(timeData);
    }
}
