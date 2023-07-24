package CocktailOfTheDay.project.Controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class GetImageController {

    @GetMapping("/img/{id}")
    public ResponseEntity<Resource> getImage (@PathVariable("id") String _filename) throws MalformedURLException {
        System.out.println("입력하신 파일명 : "+_filename);
        String uploadpath = "C:\\COTD\\recipe\\";
        Resource resource = new FileSystemResource(uploadpath+_filename);
        //파일이 존재하지 않을경우
        if(!resource.exists()){
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }else{
            HttpHeaders headers = new HttpHeaders();
            Path filepath = null;

            try {
                filepath = Paths.get(uploadpath + _filename);
                headers.add("Content-Type", Files.probeContentType(filepath));
            }catch(Exception e){
                System.out.println("파일 처리 에러");
            }
            ResponseEntity<Resource> ret = new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
            return ret;
        }
    }

}
