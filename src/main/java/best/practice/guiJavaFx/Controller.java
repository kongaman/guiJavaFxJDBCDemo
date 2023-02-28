package best.practice.guiJavaFx;


import best.practice.model.Album;
import best.practice.model.Artist;
import best.practice.model.Datasource;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;

public class Controller {
	
	@FXML
	private TableView artistTable;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	public void listArtists() {
		Task<ObservableList<Artist>> task = new GetAllArtistsTask();
		artistTable.itemsProperty().bind(task.valueProperty());
		progressBar.progressProperty().bind(task.progressProperty());
		
		progressBar.setVisible(true);
		task.setOnSucceeded(e -> progressBar.setVisible(false));
		task.setOnFailed(e -> progressBar.setVisible(false));
		
		new Thread(task).start();
	}
	
	@FXML
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
	
	@FXML
	public void updateArtist() {
		// final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
		final Artist artist = (Artist) artistTable.getItems().get(2); // normally we would use TableSelectionModel to get the selected
		Task<Boolean> task = new Task<Boolean>() {                    // item but for keeping it simple it's hardcoded to select item 2
			@Override                                                 // (3rd entry) which is "AC DC"
			protected Boolean call() throws Exception {
				return Datasource.getInstance().updateArtistName(artist.getId(), "AC/DC"); // name would normally be asked in a dialog
			}                                                                              // to simplify the example we hardcode it.
		};
		task.setOnSucceeded(e -> {
			if (task.valueProperty().get()) {
				artist.setName("AC/DC");
				artistTable.refresh();
			}
		});
		
		new Thread(task).start();
	}
	
	
}

class GetAllArtistsTask extends Task {

	@Override
	public ObservableList<Artist> call() {
		return FXCollections.observableArrayList(Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
	}
	
}
