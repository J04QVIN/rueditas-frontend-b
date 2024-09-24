package pe.edu.cibertec.rueditas_frontend_b.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_frontend_b.dto.RueditasRequestDto;
import pe.edu.cibertec.rueditas_frontend_b.dto.RueditasResponseDto;
import pe.edu.cibertec.rueditas_frontend_b.viewmodel.RueditasModel;

@Controller
@RequestMapping("/vehiculo")
public class RueditasControlador {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscarVehiculo")
    public String buscarVehiculo(Model model) {
        RueditasModel rueditasModel = new RueditasModel(
                "00", "", "", "", "", "", "");
        model.addAttribute("rueditasModel", rueditasModel);
        return "buscar";
    }

    @PostMapping("/buscandoVehiculo")
    public String buscandoVehiculo(
            @RequestParam("placa") String placa,
            Model model) {
        if (placa == null || placa.trim().isEmpty()) {
            RueditasModel rueditasModel = new RueditasModel(
                    "01", "Placa incorrecta.",
                    "", "", "", "", "");
            model.addAttribute("rueditasModel", rueditasModel);
            return "buscar";
        }

        try {
            String endpoint = "http://localhost:8081/vehiculos";
            RueditasRequestDto rueditasRequestDto = new RueditasRequestDto(placa);
            RueditasResponseDto rueditasResponseDto = restTemplate.postForObject(endpoint, rueditasRequestDto, RueditasResponseDto.class);
            if (rueditasResponseDto.codigo().equals("00")) {
                RueditasModel vehiculoModel = new RueditasModel(
                        "00", "", rueditasResponseDto.autoMarca(), rueditasResponseDto.autoModelo(), rueditasResponseDto.autoNroAsientos(),
                        rueditasResponseDto.autoPrecio(), rueditasResponseDto.autoColor());
                model.addAttribute("rueditasModel", vehiculoModel);
                return "detalles";
            } else {
                RueditasModel rueditasModel = new RueditasModel(
                        "01", "Vehículo no encontrado.",
                        "", "", "", "", "");
                model.addAttribute("rueditasModel", rueditasModel);
                return "buscar";
            }
        } catch(Exception e) {
            RueditasModel rueditasModel = new RueditasModel(
                    "99", "Error al buscar vehículo.",
                    "", "", "", "", "");
            model.addAttribute("rueditasModel", rueditasModel);
            System.out.println(e.getMessage());
            return "buscar";
        }
    }

}
