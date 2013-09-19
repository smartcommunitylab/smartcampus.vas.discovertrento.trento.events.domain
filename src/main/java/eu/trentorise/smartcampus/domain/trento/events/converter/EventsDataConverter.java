package eu.trentorise.smartcampus.domain.trento.events.converter;

import it.sayservice.platform.core.domain.actions.DataConverter;
import it.sayservice.platform.core.domain.ext.Tuple;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.domain.discovertrento.GenericEvent;
import eu.trentorise.smartcampus.services.trento.events.data.message.Events.TCEvent;

public class EventsDataConverter implements DataConverter {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");   
	
//	private static final int DURATION = (24*60 - 1)*60*1000;
	
	private static final Map<String,String> CATEGORIES = new HashMap<String, String>();
	static {
		CATEGORIES.put("Mostra", "Exhibitions");
		CATEGORIES.put("Mostra Fotografica", "Exhibitions");
		CATEGORIES.put("Musica", "Concerts");
		CATEGORIES.put("Musical", "Concerts");
		CATEGORIES.put("Musica classica", "Concerts");
		CATEGORIES.put("Musica lirica", "Concerts");
//		CATEGORIES.put("Manifestazioni ed eventi", "Manifestations");
		CATEGORIES.put("Convegno", "Seminars");
		CATEGORIES.put("Cinema", "Movies");
		CATEGORIES.put("Teatro", "Theaters");
		CATEGORIES.put("Danza", "Dances");
	}
	
	@Override
	public Serializable toMessage(Map<String, Object> parameters) {
		if (parameters == null)
			return null;
		return new HashMap<String, Object>(parameters);
	}
	
	@Override
	public Object fromMessage(Serializable object) {
		List<ByteString> data = (List<ByteString>) object;
		Tuple res = new Tuple();
		Map<String,GenericEvent> map = new HashMap<String, GenericEvent>();
		
		for (ByteString bs : data) {
			try {
				TCEvent ev = TCEvent.parseFrom(bs);
				extractGenericEvent(ev, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ArrayList<GenericEvent> list = new ArrayList<GenericEvent>(map.values());
		res.put("data", list.toArray(new GenericEvent[list.size()]));
		return res;
	}

	private void extractGenericEvent(TCEvent ev, Map<String, GenericEvent> map) throws ParseException {
		String df, dt;
		if (ev.getDateFromCount() > 0 && ev.getDateToCount() > 0) {
			df = ev.getDateFrom(0);
			dt = ev.getDateTo(ev.getDateToCount()-1);
		} else {
			df = dt = ev.getEventDate();
		}
		
		GenericEvent ge = new GenericEvent();
		ge.setDescription(createDescription(ev));
		ge.setSource("TrentinoCultura");
		ge.setTiming(ev.getTime());

		ge.setFromTime(sdf.parse(df).getTime());
		ge.setToTime(sdf.parse(dt).getTime());
		
		ge.setType(extractType(ev.getCategory()));
		
		ge.setAddressString(ev.getPlace()+(ev.hasAddress() && !ev.getAddress().isEmpty() ? ", "+ ev.getAddress():""));
		
		ge.setId(ev.getId()+"@"+ge.getSource());
		if (ev.hasPoi()) {
			ge.setPoiId(ev.getPoi().getPoiId());
		}
		ge.setTitle(ev.getTitle());
		map.put(ev.getId(), ge);
	}

	private String createDescription(TCEvent ev) {
		StringBuilder descr = new StringBuilder();
		if (ev.hasDetails())  descr.append(ev.getDetails());
		if (ev.hasNotes()) {
			if (descr.length() > 0) descr.append("<br/>");
			descr.append(ev.getNotes());
		}
		if (ev.hasPrice()) {
			if (descr.length() > 0) descr.append("<br/>");
			descr.append("Costo: ");
			descr.append(ev.getPrice());
		}
		if (ev.hasOrganization()) {
			if (descr.length() > 0) descr.append("<br/>");
			descr.append(ev.getOrganization());
		}
		if (ev.hasTel() || ev.hasMail() || ev.hasFax()) {
			if (descr.length() > 0) descr.append("<br/>");
			if (ev.hasTel()) {
				descr.append("<br/>");
				descr.append("Tel: ");
				descr.append(ev.getTel());
			}
			if (ev.hasMail()) {
				descr.append("<br/>");
				descr.append("E-mail: ");
				descr.append(ev.getMail());
			}
			if (ev.hasFax()) {
				descr.append("<br/>");
				descr.append("Fax: ");
				descr.append(ev.getFax());
			}
		}
		if (ev.hasWww()) {
			if (descr.length() > 0) descr.append("<br/>");
			descr.append(ev.getWww());
		}
		
		String s = descr.toString();
		s = s.replace("\n", "");
		s = s.replace("\t", "");
		return s;
	}

	private String extractType(String category) {
		return CATEGORIES.get(category);
	}

	private static String encode(String s) {
		return new BigInteger(s.getBytes()).toString(16);
	}
}
