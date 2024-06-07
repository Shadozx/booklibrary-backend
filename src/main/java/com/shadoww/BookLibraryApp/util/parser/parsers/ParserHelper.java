package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.models.images.Image;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import com.shadoww.BookLibraryApp.util.texformatters.TextFormatter;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElement;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserHelper {


    private ParserHelper(){}


    public static Document getDocument(String url) throws IOException {
        return Jsoup
                .connect(url)
                .header("Accept-Encoding", "gzip, deflate")
//                    .userAgent("Mozilla")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();
    }

//    public List<Document> getDocuments(List<String> urls) throws IOException {
//        return urls.stream().map(u -> getDocument(u)).toList();
//    }
//

    public static Image parseImage(String url) throws IOException {

        System.out.println(url);
        Image image = new BookImage();
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();

        image.setContentType("image/jpeg");

        image.setData(response.bodyAsBytes());

        return image;
    }

    public static List<Chapter> addNumber(List<Chapter> chapters) {
        for(int i = 1; i <= chapters.size(); i++) chapters.get(i-1).setNumberOfPage(i);

        return chapters;
    }

    //    Image addChapterImage(Element el, List<Image> images, Book book) {
    public static ChapterImage addChapterImage(Element el, Book book) {
        try {
            String src = el.absUrl("src");
            if (src.startsWith("http")) {
                ChapterImage image = new ChapterImage(parseImage(src));

                if (book != null) {


                    image.setContentType("image/jpeg");
                    image.setChapterImage(book);
//                            image.setFilename(this.book.getId() + "_" + (this.images.size() + 1) + ".jpeg");

                    return image;
                }
            }else {
                Matcher matcher = Pattern.compile("base64,(?<data>.+)").matcher(src);

                ChapterImage image = new ChapterImage();
                if (matcher.find()) {
                 String data = matcher.group("data");
                    image.setData(Base64.getDecoder().decode(data));

                    image.setContentType("image/jpeg");

                    image.setChapterImage(book);

                    return image;
                }
                return null;
            }

        } catch (IOException e) {
            System.out.println("Error about adding new image: " + e.getMessage());
            return null;
        }

        return null;
    }

    public static TextElements getParagraphImage(Element el, ChapterInstance current, Book book, List<ChapterImage> images) {

        ChapterImage image = addChapterImage(el, book);

        if(image != null) {

            current.addChapterImage(image);

            images.add(image);

            TextElement element = formatText(el.attr("src", image.getFilename()), ElementType.Image);

            if (element != null) return new TextElements(List.of(element));
            else return null;
        }

        return null;
    }

    public static TextElement formatText(Element element, ElementType type) {
        return TextFormatter.parse(element, type);
    }

}
