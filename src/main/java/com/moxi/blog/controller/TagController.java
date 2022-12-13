package com.moxi.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.blog.annotation.MethodExporter;
import com.moxi.blog.entity.Tag;
import com.moxi.blog.service.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
@RequestMapping("/api/v1/pri/admin/tag")
public class TagController {
    @Autowired
    ITagService tagService;

    /**
     * 分页显示
     *
     * @param model
     * @param current
     * @return
     */
    @MethodExporter
    @GetMapping("/pageList")
    public String pageTagVo(Model model, @RequestParam(value = "current", required = false) String current) {
        Page<Tag> tagPage = tagService.pageTag(current, "3");
        log.info("Records:{},Total:{},Size:{},Current:{}", tagPage.getRecords(), tagPage.getTotal(), tagPage.getSize(), tagPage.getCurrent());
        model.addAttribute(tagPage);
        return "admin/tag";
    }

    /**
     * 添加按钮 -> 跳转到新增输入页【只做转发】
     *
     * @return
     */
    @GetMapping("/addTag/input")
    public String toTagPage() {
        return "admin/tag-input";
    }

    /**
     * 回显数据
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editTag/{id}/input")
    public String editTag(@PathVariable("id") Long id, Model model) {
        //保存数据在request
        model.addAttribute("tag", tagService.getById(id));
        return "admin/tag-input";
    }

    /**
     * 类型是否存在
     *
     * @param tagName
     * @return
     */
    public boolean checkExist(String tagName, Long id) {
        if (tagName != null) {
            return tagService.getTagByName(tagName) > 0 ? true : false;
        }
        if (id != null) {
            return tagService.getById(id) != null ? true : false;
        }
        return false;
    }

    /**
     * 修改类型
     *
     * @param model
     * @param tag
     * @param attributes
     * @return
     */
    @PostMapping("/updateTag/{id}")
    public String updateTag(Model model, @Validated(Tag.Save.class) Tag tag, RedirectAttributes attributes) {
        //修改前先查询是否存在
        if (checkExist(tag.getName(), null)) {
            //说明这个类型已经存在
            model.addAttribute("message", "该类型名称已经存在");
            return "admin/tag-input";
        }
        if (tagService.updateById(tag)) {
            attributes.addFlashAttribute("message", "修改成功");
        } else {
            attributes.addFlashAttribute("message", "修改失败，请稍后再试");
        }
        return "redirect:/api/v1/pri/admin/tag/pageList";
    }

    /**
     * 新增类型
     *
     * @param model
     * @param tag
     * @param bindingResult
     * @param attributes
     * @return
     */
    @PostMapping("/addTag")
    public String addTag(Model model, @Validated(Tag.Save.class) Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            String errMessage = bindingResult.getFieldError().getDefaultMessage();
            model.addAttribute("message", errMessage);
            return "admin/tag-input";
        }
        if (checkExist(tag.getName(), null)) {
            model.addAttribute("message", "该标签名称已经存在");
            return "admin/tag-input";
        }
        if (tagService.saveTag(tag) > 0) {
            attributes.addFlashAttribute("message", "添加成功");
        } else {
            attributes.addFlashAttribute("message", "添加失败，请稍后再试");
        }
        return "redirect:/api/v1/pri/admin/tag/pageList";

    }

    /**
     * 删除标签
     * @param attributes
     * @param id
     * @return
     */
    @GetMapping("/deleteTag/{id}")
    public String deleteTag(RedirectAttributes attributes, @Validated(Tag.Update.class) @PathVariable("id") Long id) {
        if (checkExist(null, id) && tagService.removeById(id)) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败");
        }
        return "redirect:/api/v1/pri/admin/tag/pageList";
    }

}
