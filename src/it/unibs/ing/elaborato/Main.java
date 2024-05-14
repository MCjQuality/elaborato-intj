package it.unibs.ing.elaborato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class Main {

	public static void main(String[] args)
	{	
		try 
		{
			Districts districts = new Districts();
			districts.read(Constants.DISTRICTS_FILEPATH);

			Hierarchies hierarchies = new Hierarchies();
			hierarchies.read(Constants.HIERARCHIES_FILEPATH);

			ConversionElements conversionElements = new ConversionElements();
			conversionElements.read(Constants.CONVERSION_ELEMENTS_FILEPATH);

			Users users = new Users();
			users.read(Constants.USERS_FILEPATH);

			ExchangeProposals proposals = new ExchangeProposals();
			proposals.read(Constants.PROPOSALS_FILEPATH);
			
			ClosedSets closedSets = new ClosedSets();
			closedSets.read(Constants.CLOSED_SETS_FILEPATH);

			Scanner scanner = new Scanner(System.in);
			String aCapo = System.lineSeparator();
			scanner.useDelimiter(aCapo);
			boolean not_valid;

			do 
			{
				Utility.clearConsole(Constants.TRANSACTION_TIME);
				not_valid = false;

				System.out.println();
				System.out.println(Constants.LIGHT_BLUE_FORMAT + Constants.ITALICS + Printer.align(Constants.WELCOME_MESSAGE, Constants.MENU_LINE_SIZE) + Constants.RESET_FORMAT);
				System.out.println();
				
				String username = Utility.checkCondition(Constants.USERNAME_MESSAGGE, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);

				System.out.print(Constants.PASSWORD_MESSAGE);
				System.out.print(Constants.ESCAPE_CODE_BLACKED_TEXT);
				String psw = scanner.next();
				System.out.println(Constants.RESET_FORMAT);
				
				MenuConfig menuConfig = new MenuConfig();
				MenuConsumer menuConsumer = new MenuConsumer();

				if(users.checkUser(username, psw))
				{
					if(!users.getUser(username, psw).isConsumer()) 
					{
						Configurator configurator = (Configurator) users.getUser(username, psw);
						System.out.println(Constants.GREEN_FORMAT + Printer.align(Constants.RECOGNIZED_CREDENTIALS_MESSAGE, Constants.MENU_LINE_SIZE) + Constants.RESET_FORMAT);
						System.out.println(Printer.align(Constants.CONFIRM_CONFIGURATOR_ACCESS_MESSAGE + username, Constants.MENU_LINE_SIZE));

						if(configurator.getFirstAccess()) 
						{
							String newUsername;
							String newPsw;

							Utility.clearConsole(Constants.TRANSACTION_TIME);
							not_valid = false;

							System.out.println();
							System.out.println(Printer.align(Constants.AGGIORNAMENTO_CREDENZIALI, Constants.MENU_LINE_SIZE));
							System.out.println();

							newUsername = Utility.check2Condition(Constants.INSERT_NEW_USERNAME, Constants.INVALID_INPUT_MESSAGE, Constants.USERNAME_ALREADY_EXSIST_MESSAGE, input -> input.isBlank() || input.equals(username), users::contains, scanner);
							newPsw = Utility.check2Condition(String.format(Constants.INSERT_NEW_PSW, Constants.DIGITS_REQUIREMENT, Constants.LETTERS_REQUIREMENT), Constants.INSERT_PASSWORD_MATCHES_PREVIOUS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> input.equals(psw), input -> !Utility.isPswValid(input.toCharArray(), Constants.DIGITS_REQUIREMENT, Constants.LETTERS_REQUIREMENT), scanner);

							configurator.updateCredentials(newUsername, newPsw);
							users.write(Constants.USERS_FILEPATH);

							System.out.println();
							System.out.println(Printer.align(Constants.CREDENTIALS_UPDATED, Constants.MENU_LINE_SIZE));
						}

						Utility.clearConsole(Constants.READING_TIME);

						if(menuConfig.menuConfigManager(districts, hierarchies, conversionElements, proposals ,closedSets, scanner))
							not_valid = true;
					}
					else
					{
						System.out.println(Constants.GREEN_FORMAT + Printer.align(Constants.RECOGNIZED_CREDENTIALS_MESSAGE, Constants.MENU_LINE_SIZE) + Constants.RESET_FORMAT);
						System.out.println(Printer.align(Constants.CONFIRM_CONSUMER_ACCESS_MESSAGE + username, Constants.MENU_LINE_SIZE));

						Utility.clearConsole(Constants.READING_TIME);
						Consumer consumer = (Consumer) users.getUser(username, psw);

						if(menuConsumer.menuConsumerManager(hierarchies, scanner, consumer, conversionElements, proposals, closedSets)) 
							not_valid = true;
					}
				}
				else 
				{
					System.out.println(Constants.RED_FORMAT + Printer.align(Constants.WRONG_CREDENTIAL_MESSAGE, Constants.MENU_LINE_SIZE) + Constants.RESET_FORMAT);
					System.out.println();

					String yesOrNo = Utility.checkCondition(Constants.REGISTRATION_AS_USER_MESSAGE, Constants.INVALID_INPUT_MESSAGE, (input) -> (!input.equalsIgnoreCase(Constants.YES_MESSAGE) && !input.equalsIgnoreCase(Constants.NO_MESSAGE)), scanner);
					if(yesOrNo.equalsIgnoreCase(Constants.YES_MESSAGE)) 
					{
						Utility.clearConsole(Constants.TRANSACTION_TIME); 

						boolean input_error = false;
						int district_index;
						String district_name;
						String email;
						String username_consumer;
						String psw_consumer;

						System.out.println();
						System.out.println(Constants.GRAY_FORMAT + Printer.align(Constants.REGISTRATION_CONSUMER_MESSAGE, Constants.MENU_LINE_SIZE) + Constants.RESET_FORMAT);
						System.out.println();
						System.out.println(Printer.columnize(Printer.printNumberedDistricts(districts), Constants.MENU_LINE_SIZE));

						do 
						{
							input_error = false;

							district_index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_1_MESSAGE && Integer.parseInt(input) <= districts.getDistricts().size()), scanner));
							district_name = districts.getDistricts().get(district_index - Constants.NUMBER_1_MESSAGE).getName();
							username_consumer = Utility.check2Condition(Constants.USERNAME_MESSAGGE, Constants.INVALID_INPUT_MESSAGE, Constants.USERNAME_ALREADY_EXSIST_MESSAGE, String::isBlank, users::contains, scanner);
							psw_consumer = Utility.checkCondition(Constants.PASSWORD_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isPswValid(input.toCharArray(), Constants.DIGITS_REQUIREMENT, Constants.LETTERS_REQUIREMENT), scanner);
							email = Utility.check2Condition(Constants.MAIL_MESSAGE, Constants.INVALID_INPUT_MESSAGE, Constants.INSERT_VALID_MAIL_ADDRESS, String::isBlank, input -> !Utility.isValidEmail(input), scanner);

							Consumer consumer = new Consumer(username_consumer, BCrypt.hashpw(psw_consumer, BCrypt.gensalt()), district_name, email);
							users.add(consumer);
							users.write(Constants.USERS_FILEPATH);

							Utility.clearConsole(Constants.TRANSACTION_TIME);
							if(menuConsumer.menuConsumerManager(hierarchies, scanner, consumer, conversionElements, proposals, closedSets))
								not_valid = true;
						} while(input_error);
					}
					else
						not_valid = true;
				}
			} while(not_valid);
			scanner.close();
		} catch(FileNotFoundException e) {
			System.out.println(Constants.FILE_NOT_FOUND);
		}
		catch (IOException e) {
			System.out.println(Constants.IOEXCEPTION);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
