package com.toyota.backend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
    public byte[] drawById(int id) {

            TT_Vehicle_Defect defect = vehicleDefectRepository.findById(id).orElseThrow(
                    () -> new NullPointerException(String.format("Vehicle defect not found with id: %s", id)));
            List<TT_Defect_Location> locations = vehicleDefectLocationRepository.findLocationByDefectId(defect.getId());
            byte[] defectImg = defect.getImg();

            //an image can have multiple defect. this loop draws line for every location
            for (TT_Defect_Location location : locations ){
                //creating mat object from opencv to convert byte object to mat.
                Mat image = Imgcodecs.imdecode(new MatOfByte(defectImg), Imgcodecs.IMREAD_UNCHANGED);
                int[] x = {location.getX1(), location.getX2(), location.getX3() };
                int[] y = {location.getY1(), location.getY2(), location.getY3() };

                //asserting points
                Point point1 = new Point(x[0], y[0]);
                Point point2 = new Point(x[1], y[1]);
                Point point3 = new Point(x[2], y[2]);
                //draw line to image according to the coordinates
                Imgproc.line(image, point1, point2, new Scalar(0, 0, 255), 2);
                Imgproc.line(image, point2, point3, new Scalar(0, 0, 255), 2);
                Imgproc.line(image, point3, point1, new Scalar(0, 0, 255), 2);

                //to hold the byte array of image.
                MatOfByte matOfByte = new MatOfByte();
                //convert image mat object to jpeg format and hold it on matOfByte object.
                Imgcodecs.imencode(".jpeg", image, matOfByte);
                //convert mathofbyte to array and assign it.
                defectImg = matOfByte.toArray();
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
                logger.debug("filter is default");
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
                defectList = vehicleDefectRepository.findAll(paging);}
            else {
                defectList = vehicleDefectRepository.findAllDefectWithFilter(paging, filterYear);
            }
            if (defectList.isEmpty())
                logger.debug("list of defect is empty.");
            return DefectViewResponse.ConvertToViewResponse(defectList);
    }
}
