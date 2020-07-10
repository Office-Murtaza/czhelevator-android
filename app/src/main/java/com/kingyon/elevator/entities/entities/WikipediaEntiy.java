package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/7/8
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class WikipediaEntiy {
        private List<WikipediaBean> wikipedia;
        public List<WikipediaBean> getWikipedia() {
            return wikipedia;
        }

        public void setWikipedia(List<WikipediaBean> wikipedia) {
            this.wikipedia = wikipedia;
        }

        public static class WikipediaBean {
            /**
             * label : PARTNER
             * item : [{"id":100011,"timestamp":1594115405000,"createAccount":"admin2","createTime":1556157834000,"modifyAccount":"","modifyTime":null,"isDelete":true,"questionType":"PARTNER","name":"收益和提现","content":"<p class=\"MsoNormal\" style=\"text-indent:28.0000pt;mso-char-indent-count:2.0000;\"><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\"><font face=\"等线\">屏多多<\/font><\/span><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\">APP<\/span><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\"><font face=\"等线\">合伙人，登录<\/font>A<\/span><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\">PP<\/span><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\"><font face=\"等线\">后可通过<\/font>\u201c我的收益\u201d查看实时收益情况，并通过提现功能进行收益提现。<\/span><span style=\"mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';\nfont-size:14.0000pt;mso-font-kerning:1.0000pt;\"><o:p><\/o:p><\/span><\/p>","click":0},{"id":100012,"timestamp":1594115404000,"createAccount":"wanjixiang","createTime":1567050020000,"modifyAccount":"","modifyTime":null,"isDelete":false,"questionType":"PARTNER","name":"广告监播","content":"<p class=\"MsoNormal\"><font face=\"宋体\"><b>现在这个监控最重要的还是公共安全，主要是为了监控电梯内部的情况，加上现在电梯轿厢技术和电梯光线环境的限制，监控的角度我们可以尽量调整配合满足您这边的需求，但是想要非常清晰的看到屏幕的内容和听到声音确实很难实现，现在广告公司投放广告要监播，都是去现场监播内容的<\/b><\/font><br><\/p>","click":0},{"id":100013,"timestamp":1594115407000,"createAccount":"wanjixiang","createTime":1567050348000,"modifyAccount":"wanjixiang","modifyTime":1568086015000,"isDelete":false,"questionType":"PARTNER","name":"合伙人提现","content":"每个月15号进行结算，16-20号可进行提现","click":0},{"id":100014,"timestamp":1594115419000,"createAccount":"wanjixiang","createTime":1567050480000,"modifyAccount":"wanjixiang","modifyTime":1567473139000,"isDelete":false,"questionType":"PARTNER","name":"投放须知","content":"<p><font size=\"4\">广告审核时间周一至周五8:30----17:00<\/font><\/p><p><font size=\"4\">因系统调试，数据维护，广告上传时间由原来的<b>当日上传改为次日上传<\/b>，给您带来不便，敬请谅解<\/font><\/p><p><br><\/p>","click":0}]
             */

            private String label;
            private List<ItemBean> item;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public List<ItemBean> getItem() {
                return item;
            }

            public void setItem(List<ItemBean> item) {
                this.item = item;
            }

            public static class ItemBean {
                /**
                 * id : 100011
                 * timestamp : 1594115405000
                 * createAccount : admin2
                 * createTime : 1556157834000
                 * modifyAccount :
                 * modifyTime : null
                 * isDelete : true
                 * questionType : PARTNER
                 * name : 收益和提现
                 * content : <p class="MsoNormal" style="text-indent:28.0000pt;mso-char-indent-count:2.0000;"><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;"><font face="等线">屏多多</font></span><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;">APP</span><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;"><font face="等线">合伙人，登录</font>A</span><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;">PP</span><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;"><font face="等线">后可通过</font>“我的收益”查看实时收益情况，并通过提现功能进行收益提现。</span><span style="mso-spacerun:'yes';font-family:等线;mso-bidi-font-family:'Times New Roman';
                 font-size:14.0000pt;mso-font-kerning:1.0000pt;"><o:p></o:p></span></p>
                 * click : 0
                 */

                private int id;
                private long timestamp;
                private String createAccount;
                private long createTime;
                private String modifyAccount;
                private Object modifyTime;
                private boolean isDelete;
                private String questionType;
                private String name;
                private String content;
                private int click;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public long getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(long timestamp) {
                    this.timestamp = timestamp;
                }

                public String getCreateAccount() {
                    return createAccount;
                }

                public void setCreateAccount(String createAccount) {
                    this.createAccount = createAccount;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public String getModifyAccount() {
                    return modifyAccount;
                }

                public void setModifyAccount(String modifyAccount) {
                    this.modifyAccount = modifyAccount;
                }

                public Object getModifyTime() {
                    return modifyTime;
                }

                public void setModifyTime(Object modifyTime) {
                    this.modifyTime = modifyTime;
                }

                public boolean isIsDelete() {
                    return isDelete;
                }

                public void setIsDelete(boolean isDelete) {
                    this.isDelete = isDelete;
                }

                public String getQuestionType() {
                    return questionType;
                }

                public void setQuestionType(String questionType) {
                    this.questionType = questionType;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getClick() {
                    return click;
                }

                public void setClick(int click) {
                    this.click = click;
                }
            }
        }
}
