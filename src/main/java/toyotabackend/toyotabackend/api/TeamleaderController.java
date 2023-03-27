package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import toyotabackend.toyotabackend.service.Concrete.TeamLeaderServiceImpl;



@RestController
@RequestMapping("/teamleader")
@RequiredArgsConstructor
public class TeamleaderController
{
    private static final Logger logger = Logger.getLogger(TeamleaderController.class);

    private final TeamLeaderServiceImpl teamLeaderService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity <byte[]> getFile(@PathVariable("id") int id) {

        try {
            byte[] defect = teamLeaderService.drawById(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(defect,headers, HttpStatus.OK);

        }
        catch (Exception e){
            logger.warn("getfile in teamleader controller errored.");
            throw e;
        }
    }


}




















/*for (byte[] imageData : defect) {
                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage image = ImageIO.read(bais);

                // Create a new ByteArrayOutputStream for this image and write it in JPEG format
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);

                // Add the byte array to the list of images
                imageList.add(baos.toByteArray());

            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageList, headers, HttpStatus.OK);*/