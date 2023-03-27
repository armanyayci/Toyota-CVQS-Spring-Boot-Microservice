package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.VehicleDefectLocationRepository;
import toyotabackend.toyotabackend.dao.VehicleDefectRepository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;
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

        try {

            TT_Vehicle_Defect defect = vehicleDefectRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException("Vehicle defect not found with id -> "+ id));

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

        catch (UnsupportedOperationException e ){
            logger.warn("add operation is not supported by this list in drawing service");
            throw e;
        }
        catch (ClassCastException e){
            logger.warn("class of the specified element prevents it from being added to this list");
            throw e;
        }
        catch (NullPointerException e){
            logger.warn("specified element is null and this list does not permit null elements");
            throw e;
        }
        catch (IllegalArgumentException e){
            logger.warn("some property of this element prevents it from being added to this list");
            throw e;
        }
        catch (Exception e){
            e.printStackTrace();
            logger.warn("exception in drawbyid method of teamleader service.");
            throw e;
        }
    }
}



















            /*TT_Vehicle_Defect defect = vehicleDefectRepository.findById(24).orElseThrow
                    (()-> new NullPointerException("defect not found with id -> " + 24));

            TT_Defect_Location location = vehicleDefectLocationRepository.findById(2).orElseThrow(
                    ()-> new NullPointerException("location not found with id -> "+ 2));

            Mat image = Imgcodecs.imdecode(new MatOfByte(defect.getImg()), Imgcodecs.IMREAD_UNCHANGED);

            int[] x = { location.getX1(), location.getX2(), location.getX3() };
            int[] y = {location.getY1(), location.getY2(), location.getY3() };

            Point point1 = new Point(x[0], y[0]);
            Point point2 = new Point(x[1], y[1]);
            Point point3 = new Point(x[2], y[2]);
            Imgproc.line(image, point1, point2, new Scalar(0, 0, 255), 2);
            Imgproc.line(image, point2, point3, new Scalar(0, 0, 255), 2);
            Imgproc.line(image, point3, point1, new Scalar(0, 0, 255), 2);

            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpeg", image, matOfByte);

            return matOfByte.toArray();*/



















/*
        try {
            TT_Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException("Entity not found with id -> "+ id));

            List<TT_Vehicle_Defect> defectList = vehicleDefectRepository.findAll();

            TT_Vehicle_Defect defected = defectList.get(defectList.size()-1);

            TT_Vehicle_Defect abc = vehicleDefectRepository.findById(3).orElseThrow(
                    ()-> new NullPointerException("null pointer with 3 id"));

            byte[] imageData = abc.getImg();

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));

            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setColor(Color.red);
            int[] x = { 10, 50, 100 };
            int[] y = { 20, 60, 350 };

            for (int i = 0; i < 2; i++) {
                g2d.drawLine(1, 200, 250, 390);
            }
            g2d.dispose();

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            ImageIO.write(bufferedImage,"jpeg",output);

            defected.setImg(output.toByteArray());
            vehicleDefectRepository.save(defected);
            return output.toByteArray();
        }

        catch (IOException e)
        {
            logger.warn("Not able to create requirement io stream ");
            throw new RuntimeException();}

        catch (IllegalArgumentException e){
            logger.warn("null parameter occured");
            throw e;}
        catch (Exception e){
            logger.warn("error message");
            throw e;
        }

     */