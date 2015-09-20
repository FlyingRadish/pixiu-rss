package org.houxg.pixiurss;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table "ARTICLE".
 */
public class Article {

    private Long id;
    private String title;
    private String link;
    private Long pubTime;
    private String desc;
    private Long SourceId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient ArticleDao myDao;

    private Source source;
    private Long source__resolvedKey;


    public Article() {
    }

    public Article(Long id) {
        this.id = id;
    }

    public Article(Long id, String title, String link, Long pubTime, String desc, Long SourceId) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.pubTime = pubTime;
        this.desc = desc;
        this.SourceId = SourceId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArticleDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getPubTime() {
        return pubTime;
    }

    public void setPubTime(Long pubTime) {
        this.pubTime = pubTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getSourceId() {
        return SourceId;
    }

    public void setSourceId(Long SourceId) {
        this.SourceId = SourceId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Source getSource() {
        Long __key = this.SourceId;
        if (source__resolvedKey == null || !source__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SourceDao targetDao = daoSession.getSourceDao();
            Source sourceNew = targetDao.load(__key);
            synchronized (this) {
                source = sourceNew;
                source__resolvedKey = __key;
            }
        }
        return source;
    }

    public void setSource(Source source) {
        synchronized (this) {
            this.source = source;
            SourceId = source == null ? null : source.getId();
            source__resolvedKey = SourceId;
        }
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

}
