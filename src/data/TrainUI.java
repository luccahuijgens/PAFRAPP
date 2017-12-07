package data;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import design.LiquidCargowagon;
import design.Passengerswagon;
import design.SolidCargowagon;
import design.Train;
import design.Wagon;

public class TrainUI {
	TrainController tc = new TrainController();

	private void displayGUI() {
		//Setup of GUI
		JFrame frame = new JFrame("RichRail");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);

		JLabel label = new JLabel("Output", JLabel.LEFT);
		label.setSize(300, 30);
		label.setLocation(5, 5);

		JLabel label2 = new JLabel("Command:", JLabel.LEFT);
		label2.setSize(100, 20);
		label2.setLocation(5, 910);

		TextArea console = new TextArea();
		console.setSize(1000, 400);
		console.setLocation(1, 500);
		console.setEditable(false);

		TextArea output = new TextArea();
		output.setSize(1199, 400);
		output.setLocation(1, 50);
		output.setEditable(false);

		TextField input = new TextField();
		input.setSize(375, 20);
		input.setLocation(105, 910);

		JButton execute = new JButton("EXECUTE");
		execute.setSize(100, 30);
		execute.setLocation(500, 910);

		//Command button functions
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = input.getText();
				console.append(command + "\n");
				String value;
				// Create new train
				if (command.toLowerCase().contains("new")) {
					if (command.toLowerCase().contains("train") && idcontrol(command) == true) {
						String execution = command.replace("new train ", "");
						//check if train exists already
						if (TrainControl(execution) == false) {
							createTrain(execution);
							output.append("train " + execution + " created.\n");
						} else {
							output.append("This train already exists.\n");
						}
					}
					// create a wagon
					else if (command.toLowerCase().contains("wagon") && idcontrol(command) == true) {
						String id = "";
						//create passengerwagon
						if (command.toLowerCase().contains("passengerwagon")) {
							id += command.toLowerCase().charAt(19);
							id += command.toLowerCase().charAt(20);
							id += command.toLowerCase().charAt(21);
							//check if wagon exists already
							if (WagonControl(id)==false){
							//check for optional numseats input
							if (command.toLowerCase().contains("numseats")) {
								value = command.substring(32, command.length());
								createPassengerWagon(id, Integer.parseInt(value));
								output.append("passengerwagon " + id + " created with " + value + " seats.\n");
							} else {
								createPassengerWagon(id, 20);
								output.append("passengerwagon " + id + " created with 20 seats.\n");
							}}
							else{
								output.append("this wagon already exists.\n");
							}
						}
						//create a liquidcargowagon
						if (command.toLowerCase().contains("liquidcargowagon")) {
							id += command.toLowerCase().charAt(21);
							id += command.toLowerCase().charAt(22);
							id += command.toLowerCase().charAt(23);
							//check if wagon exists already
							if (WagonControl(id)==false){
							//check for required contentlit input
							if (command.toLowerCase().contains("contentlit")) {
								value = command.substring(36, command.length());
								createLiquidCargoWagon(id, Integer.parseInt(value));
								output.append(
										"liquidcargowagon " + id + " created with " + value + " liter content.\n");
							} else {
								output.append("no content given; try again.\n");
							}}
							else{
								output.append("this wagon already exists.\n");
							}
						}
						//create solidcargowagon
						if (command.toLowerCase().contains("solidcargowagon")) {
							id += command.toLowerCase().charAt(20);
							id += command.toLowerCase().charAt(21);
							id += command.toLowerCase().charAt(22);
							System.out.println(id);
							//check if wagon exists already
							if (WagonControl(id)==false){
							//check for required contentcub input
							if (command.toLowerCase().contains("contentcub")) {
								value = command.substring(35, command.length());
								createSolidCargoWagon(id, Integer.parseInt(value));
								output.append("solidcargowagon " + id + " created with " + value
										+ " cubic meters content.\n");
							} else {
								output.append("no content given; try again.\n");
							}
						}
						else{
							output.append("this wagon already exists.\n");
						}
					} }else {
						output.append(
								"no viable type given; use train, passengerwagon, liquidcargowagon or solid cargowagon.\n");
					}
				}
				// get numseats from passengerwagons and trains
				else if (command.toLowerCase().contains("getnumseats")) {
					//get seats from train
					if (command.toLowerCase().contains("train")) {
						String execution = command.replace("getnumseats train ", "");
						int result = getNumseatsTrain(execution);
						//check if train exists
						if (result > 0) {
							output.append("train " + execution + " has " + result + " total seats.\n");
						} else {
							output.append("train " + execution + " does not exist.\n");
						}
					}
					// get seats from passengerswagon
					else if (command.toLowerCase().contains("passengerwagon")) {
						String id = command.replace("getnumseats passengerwagon ", "");
						Wagon wag=tc.getWagon(id);
						//check if wagon is a passengerwagon
						if (wag.getClass()==Passengerswagon.class){
							Passengerswagon pw=(Passengerswagon)wag;
							output.append("passengerwagon "+id+" has "+pw.getSeats()+" seats.\n");
						}
						else{
							output.append("wagon "+id+" is not a passengerwagon.\n");
						}

					} else {
						output.append("No passengerwagon or train with the given id exists.\n");
					}
				}
				// get contentlit from liquidcargowagons and trains
				else if (command.toLowerCase().contains("getcontentlit")) {
					if (command.toLowerCase().contains("train")) {
						String execution = command.replace("getcontentlit train ", "");
						//check if train exists
						if (TrainControl(execution)==true){
						int result = getContentlitTrain(execution);
						if (result > 0) {
							output.append("train " + execution + " has " + result + " liters total content.\n");
						} else {
							output.append("train " + execution + " does not have liquidcargowagons.\n");
						}
						}
						else{
							output.append("train "+execution+" does not exist.\n");
						}
					}
					//get contentlit of liquidcargowagon
					else if (command.toLowerCase().contains("liquidcargowagon")) {
						String id = command.replace("getcontentlit liquidcargowagon ", "");
						Wagon wag=tc.getWagon(id);
						if (wag.getClass()==LiquidCargowagon.class){
							LiquidCargowagon lcw=(LiquidCargowagon)wag;
							output.append("liquidcargowagon "+id+" has a content of "+lcw.getContentliters()+" liters.\n");
						}
						else{
							output.append("wagon "+id+" is not a liquidcargowagon.\n");
						}

					} else {
						output.append("No liquidcargowagon or train with the given id exists.\n");
					}
				}
				//get contentcub of solidcargowagon
				else if (command.toLowerCase().contains("getcontentcub")) {
					if (command.toLowerCase().contains("train")) {
						String execution = command.replace("getcontentcub train ", "");
						if (TrainControl(execution)==true){
						int result = getContentcubTrain(execution);
						if (result > 0) {
							output.append("train " + execution + " has " + result + " cubic meters total content.\n");
						} else {
							output.append("train " + execution + " does not have solidcargowagons.\n");
						}
						}
						else{
							output.append("train "+execution+" does not exist.\n");
						}
					}
					//get contentcub of solidcargowagon
					else if (command.toLowerCase().contains("solidcargowagon")) {
						String id = command.replace("getcontentcub solidcargowagon ", "");
						Wagon wag=tc.getWagon(id);
						if (wag.getClass()==SolidCargowagon.class){
							SolidCargowagon scw=(SolidCargowagon)wag;
							output.append("solidcargowagon "+id+" has a content of "+scw.getContentcubic()+" cubic meters.\n");
						}
						else{
							output.append("wagon "+id+" is not a solidcargowagon.\n");
						}

					} else {
						output.append("No solidcargowagon or train with the given id exists.\n");
					}
				}
				// add wagon to train
				else if (command.toLowerCase().contains("add")) {
					String wagon_id = "";
					String train_id = "";
					wagon_id += command.toLowerCase().charAt(4);
					wagon_id += command.toLowerCase().charAt(5);
					wagon_id += command.toLowerCase().charAt(6);

					train_id += command.toLowerCase().charAt(11);
					train_id += command.toLowerCase().charAt(12);
					train_id += command.toLowerCase().charAt(13);

					Wagon wag = tc.getWagon(wagon_id);
					Train tr = tc.getTrain(train_id);
					tc.addWagon(wag, tr);
					if (tc.getWagon(wagon_id).getClass().equals(Passengerswagon.class)) {
						Passengerswagon paswag = (Passengerswagon) wag;
						//add seats of wagon to totalseats of train
						tr.setTotalseats(tr.getTotalseats() + paswag.getSeats());
						tc.updateTrain(tr);
						output.append("passengerwagon " + wag.getWagonid() + " has been added to train "
								+ tr.getTrainid() + "\n");
					}
					if (tc.getWagon(wagon_id).getClass().equals(SolidCargowagon.class)) {
						output.append("solidcargowagon " + wag.getWagonid() + " has been added to train "
								+ tr.getTrainid() + "\n");
					}
					if (tc.getWagon(wagon_id).getClass().equals(LiquidCargowagon.class)) {
						output.append("liquidcargowagon " + wag.getWagonid() + " has been added to train "
								+ tr.getTrainid() + "\n");
					}
				}
				// remove wagon from train
				else if (command.toLowerCase().contains("remove")) {
					String wagon_id = "";
					String train_id = "";
					wagon_id += command.toLowerCase().charAt(7);
					wagon_id += command.toLowerCase().charAt(8);
					wagon_id += command.toLowerCase().charAt(9);

					train_id += command.toLowerCase().charAt(16);
					train_id += command.toLowerCase().charAt(17);
					train_id += command.toLowerCase().charAt(18);

					Wagon wag = tc.getWagon(wagon_id);
					Train tr = tc.getTrain(train_id);
					tc.removeWagon(wag, tr);
					if (addWagonControl(wag, tr) == true) {
						output.append("wagon " + wag.getWagonid() + " is already in train " + tr.getTrainid() + "\n");
					} else {
						if (tc.getWagon(wagon_id).getClass().equals(Passengerswagon.class)) {
							Passengerswagon paswag = (Passengerswagon) wag;
							//remove seats of wagon from totalseats of train
							tr.setTotalseats(tr.getTotalseats() - paswag.getSeats());
							tc.updateTrain(tr);
							output.append("passengerwagon " + wag.getWagonid() + " has been removed from train "
									+ tr.getTrainid() + "\n");
						}
						if (tc.getWagon(wagon_id).getClass().equals(SolidCargowagon.class)) {
							output.append("solidcargowagon " + wag.getWagonid() + " has been removed from train "
									+ tr.getTrainid() + "\n");
						}
						if (tc.getWagon(wagon_id).getClass().equals(LiquidCargowagon.class)) {
							output.append("liquidcargowagon " + wag.getWagonid() + " has been removed from train "
									+ tr.getTrainid() + "\n");
						}
					}
				}
				// delete train or wagon
				else if (command.toLowerCase().contains("delete")) {
					String target = "";
					target += command.toLowerCase().charAt(13);
					target += command.toLowerCase().charAt(14);
					target += command.toLowerCase().charAt(15);
					//delete train
					if (command.toLowerCase().contains("train")) {
						if (tc.getTrain(target) != null) {
							tc.deleteTrain(tc.getTrain(target));
							output.append("train " + target + " deleted.\n");
						} else {
							output.append("train " + target + " does not exist.\n");
						}

					}
					//delete wagon
					if (command.toLowerCase().contains("wagon")) {
						if (tc.getWagon(target) != null) {
							tc.deleteWagon(tc.getWagon(target));
							output.append("wagon " + target + " deleted.\n");
						} else {
							output.append("wagon " + target + " does not exist.\n");
						}

					}
				}
				// command doesn't exist
				else {
					output.append(
							"given command is illegal; try new, add, remove, getnumseats, getcontentcub, getcontentlit or delete\n");
				}
			}
		});

		contentPane.add(label);
		contentPane.add(label2);
		contentPane.add(execute);
		contentPane.add(console);
		contentPane.add(output);
		contentPane.add(input);

		frame.setContentPane(contentPane);
		frame.setSize(1200, 1000);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
	//get sum of the content all attached solidcargowagons
	protected int getContentcubTrain(String execution) {
		Train t = tc.getTrain(execution);
		int result = 0;
		if (t != null) {
			for (Wagon w : t.getWagonlist()) {
				if (w.getClass().equals(SolidCargowagon.class)) {
					SolidCargowagon scw = (SolidCargowagon) w;
					result += scw.getContentcubic();
				}
			}
		}
		return result;

	}
	//get sum of the content all attached liquidcargowagons
	protected int getContentlitTrain(String execution) {
		Train t = tc.getTrain(execution);
		int result = 0;
		if (t != null) {
			for (Wagon w : t.getWagonlist()) {
				if (w.getClass().equals(LiquidCargowagon.class)) {
					LiquidCargowagon lcw = (LiquidCargowagon) w;
					result += lcw.getContentliters();
				}
			}
		}
		return result;

	}
	//get sum of the seats all attached passengerwagons
	protected int getNumseatsTrain(String execution) {
		try {
			Train t = tc.getTrain(execution);
			return t.getTotalseats();
		} catch (NullPointerException e) {
			return 0;
		}
	}
	//check if given train exists
	protected boolean TrainControl(String command) {
		boolean result = true;
		String id = "";
		id += command.charAt(0);
		id += command.charAt(1);
		id += command.charAt(2);
		if (tc.getTrain(id) == null) {
			result = false;
		}
		return result;
	}
	//check if given wagon exists
	protected boolean WagonControl(String command) {
		boolean result = true;
		String id = "";
		id += command.charAt(0);
		id += command.charAt(1);
		id += command.charAt(2);
		if (tc.getWagon(id) == null) {
			result = false;
		}
		return result;
	}
	//check if given id follows the required format
	protected boolean idcontrol(String command) {
		String id = command.replace("new train ", "");
		id = id.replace("new passengerwagon ", "");
		id = id.replace("new liquidcargowagon ", "");
		id = id.replace("new solidcargowagon ", "");
		boolean result = false;
		if (Character.isLetter(id.charAt(0)) && Character.isLetter(id.charAt(1)) && Character.isDigit(id.charAt(2))) {
			result = true;
		}
		return result;
	}

	protected void createTrain(String id) {
		tc.createTrain(id);

	}

	protected void createPassengerWagon(String id, int numseats) {
		tc.createWagon(id, numseats, "Passenger");

	}

	protected void createPassengerWagon(String id) {
		tc.createWagon(id, 20, "Passenger");

	}

	protected void createSolidCargoWagon(String id, int content) {
		tc.createWagon(id, content, "SolidCargo");

	}

	protected void createLiquidCargoWagon(String id, int content) {
		tc.createWagon(id, content, "LiquidCargo");

	}
	//check if given wagon is already attached to given train
	protected boolean addWagonControl(Wagon w, Train t) {
		boolean result = false;
		for (Wagon wag : tc.getWagons(t)) {
			if (wag.getWagonid().equals(w.getWagonid()))
				result = true;
		}
		return result;
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TrainUI().displayGUI();
			}
		});
	}
}