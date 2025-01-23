package met.petar_djordjevic_5594.gamevalut_server.utils;

import met.petar_djordjevic_5594.gamevalut_server.exception.PaginationException;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Page;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Pages;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {

    public static <T> Pages getResoultAndPages( Integer currentPage, Integer limit, Long maxNumberOfElements, List<T> resoult) {

        if (currentPage < 1)
            throw new PaginationException("Page must be postive number!");

        if (limit < 1)
            throw new PaginationException("Limit must be postive number!");

        Pages<T> pages = new Pages<T>();


        currentPage -= 1;

        final Integer maxPage =  (int) Math.ceil((double) maxNumberOfElements / limit);
        final Integer minPage = 1;

        Integer nextPagesNumber;


        if ((maxPage - (currentPage + 1)) == 0)
            nextPagesNumber = 0;
        else if ((maxPage - (currentPage + 1)) == 1)
            nextPagesNumber = 1;
        else if ((maxPage - (currentPage + 1)) == 2)
            nextPagesNumber = 2;
        else
            nextPagesNumber = 2;

        List<Page> nextPages = new ArrayList<>();

        if (nextPagesNumber != 0 && currentPage < maxPage){

            for (Integer i = 1; i <= nextPagesNumber; i++) {
                nextPages.add(new Page(currentPage + 1 + i, limit));
            }
        }

        pages.setNextPages(nextPages);

        Integer previousPageNumber;

        if(minPage == (currentPage +1))
            previousPageNumber = 0;
        else if((minPage +1) == (currentPage +1))
            previousPageNumber = 1;
        else
            previousPageNumber  =2;



        List<Page> previousPages = new ArrayList<>();

        if (previousPageNumber != 0 && currentPage < maxPage) {
            for (Integer i = currentPage; (i >= currentPage - 1 && i != 0); i--) {
                previousPages.add(new Page(i, limit));
            }
            previousPages =  previousPages.reversed();
        }

        pages.setPreviousPages(previousPages);

        pages.setResoult(resoult);

        return pages;
    }

    public static void validatePageAndLimit(Integer page, Integer limit){
        if (page < 1)
            throw new PaginationException("Page must be postive number!");

        if (limit < 1)
            throw new PaginationException("Limit must be postive number!");
    }



}
