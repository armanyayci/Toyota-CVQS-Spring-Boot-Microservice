package com.toyota.backend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.toyota.backend.dao.VehicleDefectLocationRepository;
import com.toyota.backend.dao.VehicleDefectRepository;
import com.toyota.backend.domain.TT_Defect_Location;
import com.toyota.backend.domain.TT_Vehicle_Defect;
import com.toyota.backend.dto.DefectViewResponse;
import com.toyota.backend.service.Abstract.TeamLeaderService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class provides an implementation for the {@link TeamLeaderService} interface, handling all the business logic related to
 * teamleader operations on vehicle defects. It contains methods to draw image, listing defects and getting specified defect.
 * The class uses constructor injection to inject instances of
 * {@link VehicleDefectRepository}, {@link VehicleDefectLocationRepository}
 * The class also logs information and error messages using {@link Logger} to facilitate debugging and error handling.
 */
@Service
@RequiredArgsConstructor
public class TeamLeaderServiceImpl implements TeamLeaderService {
    private static final Logger logger = Logger.getLogger(TeamLeaderServiceImpl.class);
    private final VehicleDefectRepository vehicleDefectRepository;
    private final VehicleDefectLocationRepository vehicleDefectLocationRepository;

    /**
    * Retrieves an image of a vehicle defect by its ID, and draws lines around its locations.
    * @param id the ID of the vehicle defect to retrieve.
    * @return a byte array containing the image of the vehicle defect with lines drawn around its locations.
    * @throws NullPointerException if a vehicle defect with the given ID is not found.
    */
    @Override
    public byte[] drawById(int id) throws IOException {

        TT_Vehicle_Defect defect = vehicleDefectRepository.findById(id).orElseThrow(
                () -> new NullPointerException(String.format("Vehicle defect not found with id: %s", id)));
        List<TT_Defect_Location> locations = vehicleDefectLocationRepository.findLocationByDefectId(defect.getId());

        byte[] defectImg = defect.getImg();

        //an image can have multiple defect. this loop draws line for every location
        for (TT_Defect_Location location : locations ){

            // Get the x and y coordinates for the location of defect
            int[] x = {location.getX1(), location.getX2(), location.getX3() };
            int[] y = {location.getY1(), location.getY2(), location.getY3() };

            // Create a BufferedImage from the byte array
            InputStream in = new ByteArrayInputStream(defectImg);
            BufferedImage image = ImageIO.read(in);


            // Create a Graphics2D object from the image and set the line color and stroke width
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            // Draw the lines on the image
            g2d.drawLine(x[0], y[0], x[1], y[1]);
            g2d.drawLine(x[1], y[1], x[2], y[2]);
            g2d.drawLine(x[2], y[2], x[0], y[0]);

            // Convert the updated BufferedImage back to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);
            // Dispose of the Graphics2D object to free up resources
            g2d.dispose();
            // taking the new drawn image in order to draw more than defect on it.
            defectImg = baos.toByteArray();
        }
        logger.debug(String.format("image drawn successfully byte-img: %s,defect-id: %s", Arrays.toString(defectImg),defect.getId()));
        return defectImg;
    }
    /**
     Retrieves a list of vehicle defects with a given name, optionally filtered by year.
     * @param name the name of the vehicle.
     * @param pageNo the page number of the results to retrieve.
     * @param pageSize the number of results per page to retrieve.
     * @param sortBy the field to sort the results by.
     * @param filterYear the year to filter the results by. Pass 0 to retrieve all results.
     * @return a list of DefectViewResponse objects representing the vehicle defects.
     */
    @Override
    public List<DefectViewResponse> getListOfVehicleDefectsWithName(String name, int pageNo, int pageSize, String sortBy, int filterYear) {

            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<TT_Vehicle_Defect> defectList;
            if (filterYear == 0){
                logger.debug("unfiltered search");
                defectList = vehicleDefectRepository.findDefectsByVehicleId(name,paging);
            }
            else {
                logger.debug(String.format("filter is : %s",filterYear));
                defectList = vehicleDefectRepository.findDefectsByVehicleNameWithFilter(name,paging,filterYear);
            }
            if (defectList.isEmpty())
                logger.debug("list of defect with vehicle is empty");

            return DefectViewResponse.ConvertToViewResponse(defectList);
    }
    /**
     Retrieves a list of all vehicle defects, optionally filtered by year.
     @param pageNo the page number of the results to retrieve.
     @param pageSize the number of results per page to retrieve.
     @param sortBy the field to sort the results by.
     @param filterYear the year to filter the results by. Pass 0 to retrieve all results.
     @return a list of DefectViewResponse objects representing the vehicle defects.
     */
    @Override
    public List<DefectViewResponse> getListOfDefects(int pageNo, int pageSize, String sortBy, int filterYear) {

            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<TT_Vehicle_Defect> defectList;

            if (filterYear == 0){
                logger.debug("unfiltered search");
                defectList = vehicleDefectRepository.findAll(paging);}
            else {
                logger.debug(String.format("filtered search, filter-year:%s",filterYear));
                defectList = vehicleDefectRepository.findAllDefectWithFilter(paging, filterYear);
            }
            if (defectList.isEmpty())
                logger.debug("list of defect is empty.");
            return DefectViewResponse.ConvertToViewResponse(defectList);
    }
}
