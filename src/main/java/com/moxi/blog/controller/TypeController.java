package com.moxi.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.annotation.MethodExporter;
import com.moxi.blog.entity.Type;
import com.moxi.blog.service.ITypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated; //是@Valid 的一次封装，是Spring提供的校验机制使用
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author moxi
 * @since 2022-10-24
 */
@Slf4j
@Controller
@RequestMapping("/api/v1/pri/admin/type")
public class TypeController {
    @Autowired
    ITypeService typeService;

    /**
     * 分页显示
     *
     * @param model
     * @param current
     * @return
     */
    @MethodExporter
    @GetMapping("/pageList")
    public String pageTypeVo(Model model, @RequestParam(value = "current", required = false) String current) {
        Page<Type> typePage = typeService.pageType(current, "3");
        log.info("Records:{},Total:{},Size:{},Current:{}", typePage.getRecords(), typePage.getTotal(), typePage.getSize(), typePage.getCurrent());
        model.addAttribute(typePage);
        return "admin/type";
    }

    /**
     * 添加按钮 -> 跳转到新增输入页【只做转发】
     *
     * @return
     */
    @GetMapping("/addType/input")
    public String toTypePage() {
        return "admin/type-input";
    }

    /**
     * 回显数据
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editType/{id}/input")
    public String editType(@PathVariable("id") Long id, Model model) {
        //保存数据在request
        model.addAttribute("type", typeService.getById(id));
        return "admin/type-input";
    }

    /**
     * 类型是否存在
     *
     * @param typeName
     * @return
     */
    public boolean checkExist(String typeName, Long id) {
        if (typeName != null) {
            return typeService.getTypeByName(typeName) > 0 ? true : false;
        }
        if (id != null) {
            return typeService.getById(id) != null ? true : false;
        }
        return false;
    }

    /**
     * 修改类型
     *
     * @param model
     * @param type
     * @param attributes
     * @return
     */
    @PostMapping("/updateType/{id}")
    public String updateType(Model model, @Validated(Type.Save.class) Type type, RedirectAttributes attributes) {
        //修改前先查询是否存在
        if (checkExist(type.getName(), null)) {
            //说明这个类型已经存在
            model.addAttribute("message", "该类型名称已经存在");
            return "admin/type-input";
        }
        if (typeService.updateById(type)) {
            attributes.addFlashAttribute("message", "修改成功");
        } else {
            attributes.addFlashAttribute("message", "修改失败，请稍后再试");
        }
        return "redirect:/api/v1/pri/admin/type/pageList";
    }

    /**
     * 新增类型
     *
     * @param model
     * @param type
     * @param bindingResult
     * @param attributes
     * @return
     */
    @PostMapping("/addType")
    public String addType(Model model, @Validated(Type.Save.class) Type type, BindingResult bindingResult, RedirectAttributes attributes) {
        //判断type.name为空的策略不生效--》@TableField(insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY)未解决
        if (bindingResult.hasErrors()) {
            String errMessage = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("message", errMessage);
            return "admin/type-input";
        }
        if (checkExist(type.getName(), null)) {
            model.addAttribute("message", "该类型名称已经存在");
            return "admin/type-input";
        }
        if (typeService.saveType(type) > 0) {
            attributes.addFlashAttribute("message", "添加成功");
        } else {
            attributes.addFlashAttribute("message", "添加失败，请稍后再试");
        }
        return "redirect:/api/v1/pri/admin/type/pageList";

    }

    /**
     * 删除类型
     * @param attributes
     * @param id
     * @return
     */
    @GetMapping("/deleteType/{id}")
    public String deleteType(RedirectAttributes attributes, @Validated(Type.Update.class) @PathVariable("id") Long id) {
        if (checkExist(null, id) && typeService.removeById(id)) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败");
        }
        return "redirect:/api/v1/pri/admin/type/pageList";
    }

}
