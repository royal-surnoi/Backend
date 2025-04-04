package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Device;
import fusionIQ.AI.V2.fusionIq.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Device findByToken(String token) {
        return deviceRepository.findByToken(token);
    }

    public void save(Device device) {
        deviceRepository.save(device);
    }

    public List<Device> findByUserId(Long userId) {
        return deviceRepository.findByUserId(userId);
    }
}

