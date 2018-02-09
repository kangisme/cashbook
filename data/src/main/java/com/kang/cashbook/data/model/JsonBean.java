package com.kang.cashbook.data.model;

import java.util.List;

/**
 * Created by kangren on 2018/2/9.
 */

public class JsonBean
{
    /**
     * id : cashbook://home title : 首页 modules :
     * [{"id":"home_cate","title":"分类","dlist":[{"title":"推荐"},{"title":"分类"},{"title":"排行"},{"title":"我的"}]}]
     */

    private String id;

    private String title;

    private List<ModulesBean> modules;

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
         * id : home_cate title : 分类 dlist :
         * [{"title":"推荐"},{"title":"分类"},{"title":"排行"},{"title":"我的"}]
         */

        private String id;

        private String title;

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

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }
        }
    }
}
