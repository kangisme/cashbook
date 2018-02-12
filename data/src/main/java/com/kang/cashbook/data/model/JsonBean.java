package com.kang.cashbook.data.model;

import java.util.List;

/**
 * Created by kangren on 2018/2/9.
 */

public class JsonBean
{
    /**
     * link : cashbook://home title : 首页 modules :
     * [{"link":"home_cate","title":"分类","dlist":[{"title":"推荐"},{"title":"分类"},{"title":"排行"},{"title":"我的"}]}]
     */

    private String link;

    private String title;

    private List<ModulesBean> modules;

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<ModulesBean> getModules()
    {
        return modules;
    }

    public void setModules(List<ModulesBean> modules)
    {
        this.modules = modules;
    }

    public static class ModulesBean
    {
        /**
         * link : home_cate title : 分类 dlist :
         * [{"title":"推荐"},{"title":"分类"},{"title":"排行"},{"title":"我的"}]
         */

        private String id;

        private String title;

        private String url;

        private float scale;

        /**
         * 模板ViewHolder类型识别码
         */
        private int type;

        private List<DlistBean> dlist;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public float getScale()
        {
            return scale;
        }

        public void setScale(float scale)
        {
            this.scale = scale;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        public List<DlistBean> getDlist()
        {
            return dlist;
        }

        public void setDlist(List<DlistBean> dlist)
        {
            this.dlist = dlist;
        }

        public static class DlistBean
        {
            /**
             * title : 推荐
             */

            private String title;

            private String dlink;

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getDlink()
            {
                return dlink;
            }

            public void setDlink(String dlink)
            {
                this.dlink = dlink;
            }
        }
    }
}
