package met.petar_djordjevic_5594.gamevalut_server.model.pagination;

import java.util.List;

public class Pages<T> {
    List<Page> previousPages;
    List<Page> nextPages;
    List<T> resoult;

    public Pages() {
    }

    public List<Page> getPreviousPages() {
        return previousPages;
    }

    public void setPreviousPages(List<Page> previousPages) {
        this.previousPages = previousPages;
    }

    public List<Page> getNextPages() {
        return nextPages;
    }

    public void setNextPages(List<Page> nextPages) {
        this.nextPages = nextPages;
    }

    public List<T> getResoult() {
        return resoult;
    }

    public void setResoult(List<T> resoult) {
        this.resoult = resoult;
    }

}
