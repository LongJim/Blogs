package xlong.blogs.web.controller.front;

import xlong.blogs.model.domain.Comment;
import xlong.blogs.model.domain.Gallery;
import xlong.blogs.model.domain.Post;
import xlong.blogs.model.dto.BlogConst;
import xlong.blogs.model.enums.BlogPropertiesEnum;
import xlong.blogs.model.enums.CommentStatusEnum;
import xlong.blogs.model.enums.PostTypeEnum;
import xlong.blogs.model.enums.TrueFalseEnum;
import xlong.blogs.service.CommentService;
import xlong.blogs.service.GalleryService;
import xlong.blogs.service.PostService;
import xlong.blogs.utils.CommentUtil;
import xlong.blogs.web.controller.core.BaseController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <pre>
 *     前台内置页面，自定义页面控制器
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/4/26
 */
@Controller
public class FrontPageController extends BaseController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    /**
     * 跳转到图库页面
     *
     * @return 模板路径/themes/{theme}/gallery
     */
    @GetMapping(value = "/gallery")
    public String gallery(Model model) {
        List<Gallery> galleries = galleryService.findAllGalleries();
        model.addAttribute("galleries", galleries);
        return this.render("gallery");
    }

    /**
     * 友情链接
     *
     * @return 模板路径/themes/{theme}/links
     */
    @GetMapping(value = "/links")
    public String links() {
        return this.render("links");
    }

    /**
     * 渲染自定义页面
     *
     * @param postUrl 页面路径
     * @param model   model
     * @return 模板路径/themes/{theme}/post
     */
    @GetMapping(value = "/p/{postUrl}")
    public String getPage(@PathVariable(value = "postUrl") String postUrl, Model model) {
        Post post = postService.findByPostUrl(postUrl, PostTypeEnum.POST_TYPE_PAGE.getDesc());
        if (null == post) {
            return this.renderNotFound();
        }
        List<Comment> comments = null;
        if (StringUtils.equals(BlogConst.OPTIONS.get(BlogPropertiesEnum.NEW_COMMENT_NEED_CHECK.getProp()), TrueFalseEnum.TRUE.getDesc()) || BlogConst.OPTIONS.get(BlogPropertiesEnum.NEW_COMMENT_NEED_CHECK.getProp()) == null) {
            comments = commentService.findCommentsByPostAndCommentStatus(post, CommentStatusEnum.PUBLISHED.getCode());
        } else {
            comments = commentService.findCommentsByPostAndCommentStatusNot(post, CommentStatusEnum.RECYCLE.getCode());
        }
        model.addAttribute("is_page",true);
        model.addAttribute("post", post);
        model.addAttribute("comments", CommentUtil.getComments(comments));
        model.addAttribute("commentsCount", comments.size());
        postService.updatePostView(post);
        return this.render("page");
    }
}
