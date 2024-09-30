<template>
    <Editor :init="init" :disabled="disabled" @onClick="onClick" />
</template>

<script>
import cos from '@/common/cos.ts';

import Editor from '@tinymce/tinymce-vue';

//引入node_modules里的tinymce相关文件文件
import tinymce from 'tinymce/tinymce'; //tinymce默认hidden，不引入则不显示编辑器
import 'tinymce/themes/silver'; //编辑器主题，不引入则报错
import 'tinymce/icons/default'; //引入编辑器图标icon，不引入则不显示对应图标
import 'tinymce/models/dom';

import 'tinymce/plugins/advlist'; //高级列表
import 'tinymce/plugins/anchor'; //锚点
import 'tinymce/plugins/autolink'; //自动链接
import 'tinymce/plugins/autoresize'; //编辑器高度自适应,注：plugins里引入此插件时，Init里设置的height将失效
import 'tinymce/plugins/autosave'; //自动存稿
import 'tinymce/plugins/charmap'; //特殊字符
import 'tinymce/plugins/code'; //编辑源码
import 'tinymce/plugins/codesample'; //代码示例
import 'tinymce/plugins/directionality'; //文字方向
import 'tinymce/plugins/fullscreen'; //全屏
// import 'tinymce/plugins/image'; //插入编辑图片
import 'tinymce/plugins/insertdatetime'; //插入日期时间
import 'tinymce/plugins/link'; //超链接
import 'tinymce/plugins/lists'; //列表插件
// import 'tinymce/plugins/media'; //插入编辑媒体
import 'tinymce/plugins/nonbreaking'; //插入不间断空格
import 'tinymce/plugins/preview'; //预览
import 'tinymce/plugins/save'; //保存
import 'tinymce/plugins/searchreplace'; //查找替换
import 'tinymce/plugins/table'; //表格
import 'tinymce/plugins/template'; //内容模板
import 'tinymce/plugins/visualblocks'; //显示元素范围
import 'tinymce/plugins/visualchars'; //显示不可见字符
import 'tinymce/plugins/wordcount'; //字数统计

export default {
    name: 'TEditor',
    components: {
        Editor
    },
    props: {
        value: {
            type: String,
            default: ''
        },
        disabled: {
            type: Boolean,
            default: false
        },
        plugins: {
            type: [String, Array],
            default:
                'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap nonbreaking anchor insertdatetime advlist lists wordcount autosave'
        },
        toolbar: {
            type: [String, Array],
            default:
                'fullscreen undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link | alignleft aligncenter alignright alignjustify outdent indent | \
                fontsize | bullist numlist | blockquote subscript superscript removeformat | table image media charmap hr preview | indent2em lineheight formatpainter axupimgs'
        },
        uploadPath: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            init: {
                language_url: '/tinymce/langs/zh-Hans.js', //引入语言包文件
                language: 'zh-Hans', //语言类型

                skin_url: '../tinymce/skins/ui/oxide', //皮肤：浅色
                content_css: '../tinymce/skins/content/default/content.css', //以css文件方式自定义可编辑区域的css样式，css文件需自己创建并引入

                plugins: this.plugins, //插件配置
                toolbar_mode: 'wrap',
                toolbar: this.toolbar, //工具栏配置，设为false则隐藏
                menubar: false, //菜单栏配置，设为false则隐藏，不配置则默认显示全部菜单，也可自定义配置--查看 http://tinymce.ax-z.cn/configure/editor-appearance.php --搜索“自定义菜单”

                font_size_formats:
                    '12px 14px 16px 18px 20px 22px 24px 28px 32px 36px 48px 56px 72px', //字体大小
                lineheight_formats: '0.5 0.8 1 1.2 1.5 1.75 2 2.5 3 4 5', //行高配置，也可配置成"12px 14px 16px 20px"这种形式

                height: 400, //注：引入autoresize插件时，此属性失效
                placeholder: '在这里输入文字',
                branding: false, //tiny技术支持信息是否显示
                resize: false, //编辑器宽高是否可变，false-否,true-高可变，'both'-宽高均可，注意引号
                elementpath: false, //元素路径是否显示
                br_in_pre: true,

                content_style: 'img {max-width:100%;}', //直接自定义可编辑区域的css样式

                paste_data_images: true, //图片是否可粘贴
                file_picker_callback: function (callback, value, meta) {
                    let input = document.createElement('input');
                    input.setAttribute('type', 'file');

                    if (meta.filetype === 'image') {
                        input.setAttribute(
                            'accept',
                            '.jpeg,.jpg,.png,.gif,.svg,.bmp,.webp,.ico,.tiff,.tif'
                        );
                    }
                    if (meta.filetype === 'media') {
                        input.setAttribute('accept', '.mp4,.webm');
                    }

                    input.onchange = function () {
                        if (this.files.length > 0) {
                            let file = this.files[0];
                            cos.upload(file, this.uploadPath).then((result) => {
                                callback(result);
                            });
                        }
                    };
                    //触发点击
                    input.click();
                },
                images_upload_handler: (blobInfo, progress) => {
                    return new Promise((resolve, reject) => {
                        cos.upload(blobInfo.blob(), this.uploadPath)
                            .then((result) => {
                                progress(100);
                                resolve(result);
                            })
                            .catch((err) => {
                                reject(err);
                            });
                    });
                }
            }
        };
    },
    watch: {},
    created() {},
    mounted() {
        tinymce.init({});
    },
    methods: {
        onClick(e) {
            this.$emit('onClick', e, tinymce);
        }
    }
};
</script>

<style>
.tox-tinymce-aux {
    z-index: 9000 !important;
}
</style>
