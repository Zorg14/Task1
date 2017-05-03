package my.vaadin.application;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForum extends FormLayout 
{
	private TextField name = new TextField("Hotel name");
	private TextField address = new TextField("Hotel address"); 
	private TextField url = new TextField("booking.com link");
	private TextField feedback = new TextField("Customer feedback");
	
	private Button saveHotel = new Button("Save"); 
	private Button deleteHotel = new Button("Delete");
	
	private HotelService service = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	public HotelForum(MyUI myUI) 
	{
		this.myUI = myUI;
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveHotel, deleteHotel);
		addComponents(name, address, url, feedback, buttons);
		
		saveHotel.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveHotel.setClickShortcut(KeyCode.ENTER);
		
		binder.bindInstanceFields(this);
		
		saveHotel.addClickListener(e -> save());
		deleteHotel.addClickListener(e -> delete());
	}
	
	public void setHotel(Hotel hotel)
	{
		this.hotel = hotel;
		binder.setBean(hotel);
		
		deleteHotel.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
	}
	
	private void delete()
	{
		service.delete(hotel);
		myUI.updateList();
		setVisible(false);
	}
	
	private void save()
	{
		service.save(hotel);
		myUI.updateList();
		setVisible(false);
	}
}
