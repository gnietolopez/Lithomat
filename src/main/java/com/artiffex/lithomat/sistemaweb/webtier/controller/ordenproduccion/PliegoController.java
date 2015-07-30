package com.artiffex.lithomat.sistemaweb.webtier.controller.ordenproduccion;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.artiffex.lithomat.sistemaweb.businesstier.entity.JsonResponse;
import com.artiffex.lithomat.sistemaweb.businesstier.entity.Pliego;
import com.artiffex.lithomat.sistemaweb.businesstier.entity.TipoTrabajoDetalle;
import com.artiffex.lithomat.sistemaweb.businesstier.service.interfaz.PliegoService;
import com.artiffex.lithomat.sistemaweb.businesstier.service.interfaz.TipoVueltaService;
import com.artiffex.lithomat.sistemaweb.businesstier.utilidades.ComboSelect;

@Controller
@RequestMapping("/pliego")
public class PliegoController {
	
	private static final Logger log = Logger.getLogger(PliegoController.class);
	
	@Resource
	private PliegoService pliegoService;
	@Resource
	private TipoVueltaService tipoVueltaService;
	

	@Secured({"ROLE_ROOT","ROLE_ADMIN","ROLE_COTIZADOR"})
	@RequestMapping(value = "/calcula", method = RequestMethod.GET)
	public String calculaPliego(
			@RequestParam(value = "id_tipo_trabajo_detalle", required = false) Integer idTipoTrabajoDetalle,
			Model model
		) {
		log.info("/calcula_pliego");

		// envia datos del combo select
		List<ComboSelect> listaTipoVuelta = tipoVueltaService.listaComboSelect();
		List<Pliego> listaPliego = pliegoService.calculaListaPliegos(idTipoTrabajoDetalle);

		// envia id_tipo_trabajo_detalle para que se imprimr en jsp
		model.addAttribute("id_tipo_trabajo_detalle", idTipoTrabajoDetalle);
		model.addAttribute("listaTipoVuelta", listaTipoVuelta);
		model.addAttribute("listaPliego", listaPliego);

		listaTipoVuelta = null;
		listaPliego 	= null;
		
		return "produccion/pliego";
	} // calculaPliego
	
	@Secured({"ROLE_ROOT","ROLE_ADMIN","ROLE_COTIZADOR"})
	@RequestMapping(value = "/agrega", method = RequestMethod.POST)
	@ResponseBody
	public String agregaPliego(
			@RequestParam(value = "id_tipo_trabajo_detalle", 	required = false) Integer idTipoTrabajoDetalle,
			@RequestParam(value = "rebase_en_milimetros", 		required = false) Integer rebaseEnMilimetros,
			@RequestParam(value = "medianiles_en_milimetros", 	required = false) Integer medianilesEnMilimetros,
			@RequestParam(value = "pinzas_en_centimetros", 		required = false) Integer pinzasEnCentimetros,
			@RequestParam(value = "json", 						required = false) String json
		) {
		log.info("/agrega_pliego");
		
		Pliego pliego = new Pliego();
			TipoTrabajoDetalle tipoTrabajoDetalle = new TipoTrabajoDetalle();
			tipoTrabajoDetalle.setIdTipoTrabajoDetalle(idTipoTrabajoDetalle);
		pliego.setTipoTrabajoDetalle(tipoTrabajoDetalle);
		pliego.setRebaseEnMilimetros(rebaseEnMilimetros);
		pliego.setMedianilesEnMilimetros(medianilesEnMilimetros);
		pliego.setPinzasEnCentimetros(pinzasEnCentimetros);
		pliego.setActivo(true);
		
		pliegoService.creaPliego(pliego, json);	
		
		tipoTrabajoDetalle 	= null;
		pliego 				= null;

		return pliegoService.buscaHTML(idTipoTrabajoDetalle);
	} // agregaPliego
	
	@Secured({"ROLE_ROOT","ROLE_ADMIN","ROLE_COTIZADOR"})
	@RequestMapping(value="/activa_lista", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse activaListaPliegos(
			@RequestParam(value = "json", required = false) String json
		) {
		log.info("/activa_lista_pliegos");
		
		JsonResponse jsonResponse = new JsonResponse();
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(json);
			JSONArray array = (JSONArray)obj;
			for(int i=0; i<array.size(); i++) {
				System.out.println(array.get(i));
				pliegoService.activaPliego( Integer.parseInt( array.get(i).toString() ) );
			}
			jsonResponse.setEstatusOperacion(1);
		} catch(Exception e) {
			e.printStackTrace();
			jsonResponse = null;
		}
		return jsonResponse;
	} // activaListaPliegos
	
}
