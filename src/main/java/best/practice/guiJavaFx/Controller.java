package best.practice.guiJavaFx;


import best.practice.model.Album;
import best.practice.model.Artist;
import best.practice.model.Datasource;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {
	
	@FXML
	private TableView artistTable;
	
	public void listArtists() {
		Task<ObservableList<Artist>> task = new GetAllArtistsTask();
		artistTable.itemsProperty().bind(task.valueProperty());
		
		new Thread(task).start();
	}
	
	public void listAlbumsForArtist() {
		final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
		if(artist == null) {
			System.out.println("No artist selected");
			return;
		}
		Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
			@Override
			protected ObservableList<Album> call(){
				return FXCollections.observableArrayList(Datasource.getInstance().queryAlbumsForArtistId(artist.getId()));
			}
		};
		artistTable.itemsProperty().bind(task.valueProperty());
		
		new Thread(task).start();
	}
	
	
}

class GetAllArtistsTask extends Task {

	@Override
	public ObservableList<Artist> call() {
		return FXCollections.observableArrayList(Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
	}
	
}
