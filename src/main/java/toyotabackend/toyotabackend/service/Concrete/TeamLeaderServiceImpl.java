package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.VehicleDefectLocationRepository;
import toyotabackend.toyotabackend.dao.VehicleDefectRepository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;
import toyotabackend.toyotabackend.dto.response.DefectViewResponse;
import toyotabackend.toyotabackend.service.Abstract.TeamLeaderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamLeaderServiceImpl implements TeamLeaderService {
    private static final Logger logger = Logger.getLogger(TeamLeaderServiceImpl.class);
    private final VehicleDefectRepository vehicleDefectRepository;
    private final VehicleDefectLocationRepository vehicleDefectLocationRepository;

    @Override
    public byte[] drawById(int id) {

            TT_Vehicle_Defect defect = vehicleDefectRepository.findById(id).orElseThrow(
                    () -> new NullPointerException(String.format("Vehicle defect not found with id: %s", id)));

            List<TT_Defect_Location> locations = vehicleDefectLocationRepository.findLocationByDefectId(defect.getId());

            byte[] defectImg = defect.getImg();

            for (TT_Defect_Location location : locations ){

                Mat image = Imgcodecs.imdecode(new MatOfByte(defectImg), Imgcodecs.IMREAD_UNCHANGED);
                int[] x = {location.getX1(), location.getX2(), location.getX3() };
                int[] y = {location.getY1(), location.getY2(), location.getY3() };

                Point point1 = new Point(x[0], y[0]);
                Point point2 = new Point(x[1], y[1]);
                Point point3 = new Point(x[2], y[2]);
                Imgproc.line(image, point1, point2, new Scalar(0, 0, 255), 2);
                Imgproc.line(image, point2, point3, new Scalar(0, 0, 255), 2);
                Imgproc.line(image, point3, point1, new Scalar(0, 0, 255), 2);

                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpeg", image, matOfByte);

                defectImg = matOfByte.toArray();
            }
            return defectImg;
        }
    @Override
    public List<DefectViewResponse> getListOfVehicleDefectsWithName(String name, int pageNo, int pageSize, String sortBy, int filterYear) {

            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<TT_Vehicle_Defect> defectList;

            if (filterYear == 0){
                defectList = vehicleDefectRepository.findDefectsByVehicleId(name,paging);
            }
            else {
                defectList = vehicleDefectRepository.findDefectsByVehicleNameWithFilter(name,paging,filterYear);
            }

            if (defectList.isEmpty())
                logger.info("list of defect with vehicle is empty");

            return DefectViewResponse.ConvertToViewResponse(defectList);
        }
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
                logger.info("list of defect is empty.");

            return DefectViewResponse.ConvertToViewResponse(defectList);
    }
}
