package example;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Document extends Entity implements Trackable {

    public String content;
    private Date creationDate;
    private Date lastModificationDate;
    public static final int DOCUMENT_ENTITY_CODE = 12;

    public Document (String content){
        this.content = content;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    @Override
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public int getEntityCode() {
        return DOCUMENT_ENTITY_CODE;
    }

    @Override
    public Document copy() {
        Document copyDocument = new Document(this.content);
        copyDocument.id = this.id;
        copyDocument.creationDate = new Date(this.creationDate.getTime());
        copyDocument.lastModificationDate = new Date(this.lastModificationDate.getTime());
        return copyDocument;
    }

}
