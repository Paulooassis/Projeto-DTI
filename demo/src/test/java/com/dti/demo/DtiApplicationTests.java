package com.dti.demo;

import com.dti.demo.dto.DroneDTO;
import com.dti.demo.dto.ZonaDeExclusaoDTO;
import com.dti.demo.dto.PedidoDTO;
import com.dti.demo.dto.RelatorioDTO;
import com.dti.demo.service.DroneService;
import com.dti.demo.service.ZonaDeExclusaoService;
import com.dti.demo.service.EntregaService;
import com.dti.demo.service.RelatorioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DtiApplicationTests {

	@Autowired
	private DroneService droneService;

	@Autowired
	private ZonaDeExclusaoService zonaDeExclusaoService;

	@Autowired
	private EntregaService entregaService;

	@Autowired
	private RelatorioService relatorioService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(droneService);
	}

	@Test
	void criarDroneTest() {
		DroneDTO drone = DroneDTO.builder()
				.bateria(100)
				.disponivel(true)
				.estado("IDLE")
				.pesoSuportado(10.0)
				.distanciaSuportada(100.0)
				.build();

		DroneDTO salvo = droneService.criarDrone(drone);
		Assertions.assertNotNull(salvo.getId());
		Assertions.assertEquals(100, salvo.getBateria());
	}

	@Test
	void adicionarZonaDeExclusaoTest() {
		ZonaDeExclusaoDTO zona = ZonaDeExclusaoDTO.builder()
				.x(10)
				.y(10)
				.raio(5)
				.build();

		ZonaDeExclusaoDTO adicionada = zonaDeExclusaoService.adicionar(zona);
		Assertions.assertEquals(10, adicionada.getX());
		Assertions.assertTrue(zonaDeExclusaoService.estaDentroDeZonaRestrita(12, 12));
		Assertions.assertFalse(zonaDeExclusaoService.estaDentroDeZonaRestrita(30, 30));
	}

	@Test
	void alocarEntregasTest() {
		List<PedidoDTO> pedidos = entregaService.alocarPedidosParaDrone();
		Assertions.assertNotNull(pedidos);
	}

	@Test
	void gerarRelatorioTest() {
		RelatorioDTO relatorio = relatorioService.gerarRelatorio();
		Assertions.assertNotNull(relatorio);
		Assertions.assertTrue(relatorio.getTotalEntregas() >= 0);
	}
}
