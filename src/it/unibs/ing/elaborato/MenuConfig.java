package it.unibs.ing.elaborato;

import java.io.IOException;
import java.util.Scanner;

/**
 * Classe che si occupa della gestione del menu.
 * Presenta all'utente delle opzioni e ne supervisiona il comportamento attraverso opportuni controlli sull'input dei dati.
 */
public class MenuConfig {

	private String createMenuConfig()
	{
		StringBuilder menuBuffer = new StringBuilder();

		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Printer.align(Constants.MENU_MESSAGE, Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_1_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_2_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_3_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_4_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_5_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_6_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_7_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_8_MENU_MESSAGE);
		menuBuffer.append(Constants.DOUBLE_NEW_LINE);
		menuBuffer.append(Constants.OPTION_0_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);

		return menuBuffer.toString();
	}

	public boolean menuConfigManager(Districts districts, Hierarchies hierarchies, ConversionElements conversionElements, ExchangeProposals proposals, ClosedSets closedSets, Scanner scanner) throws IOException, CloneNotSupportedException
	{
		int index;

		do 
		{
			System.out.print(createMenuConfig());
			index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 8), scanner));
			chooseOptionMenu(index, districts, hierarchies, conversionElements, proposals, closedSets, scanner);
		} while(index != Constants.NUMBER_0_MESSAGE && index != Constants.NUMBER_8_MESSAGE);

		return index == 8;
	}

	private void chooseOptionMenu(int choice, Districts districts, Hierarchies hierarchies, ConversionElements conversionElements, ExchangeProposals proposals, ClosedSets closedSets, Scanner scanner) throws CloneNotSupportedException, IOException
	{
		switch (choice)
		{
		case Constants.NUMBER_1_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			insertNewDistrict(districts, scanner);
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_2_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			insertNewHierarchy(hierarchies,conversionElements , scanner);
			break;
		case Constants.NUMBER_3_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showDistricts(districts);
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_4_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showHierarchies(hierarchies, scanner);
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_5_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showConvFact(hierarchies, conversionElements, scanner);
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_6_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showProposals(hierarchies, proposals, scanner);
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_7_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showClosedSets(closedSets);
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		case Constants.NUMBER_8_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			System.out.println(Constants.LOGOUT_MESSAGGE);
			break;
		case Constants.NUMBER_0_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			System.out.println(Constants.TERMINATION_MENU_MESSAGE);
			Utility.clearConsole(Constants.READING_TIME);
			break;
		default: System.out.println(Constants.INSERT_VALID_OPTION_MENU_MESSAGE);
		}
	}

	private void insertNewDistrict(Districts districts, Scanner scanner) throws IOException
	{
		System.out.println();
		System.out.println(Printer.align(Constants.INSERT_NEW_DISTRICT_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		String name = Utility.check2Condition(Constants.INSERT_DISTRICT_NAME_MESSAGE, Constants.INVALID_INPUT_MESSAGE, Constants.DISTRICT_ALREADY_INSERTED, String::isBlank, districts::isDistrictDuplicate, scanner);
		District district = new District(name);

		System.out.println(Constants.INSERT_MUNICIPALITY_BELONG_DISTRICT_MESSAGE);
		String country;
		do 
		{
			System.out.print(Constants.SEPARATOR);
			country = scanner.next();
			if (!country.equalsIgnoreCase(Constants.Q_MESSAGE) && Utility.isPresent(Constants.ITALIAN_MUNICIPALITIES_FILEPATH, country) && !districts.isMunicipalityDuplicate(country) && !district.isMunicipalityDuplicate(country))
				district.add(country);
			else if(country.equalsIgnoreCase(Constants.Q_MESSAGE))
				System.out.println();
			else if(districts.isMunicipalityDuplicate(country) || district.isMunicipalityDuplicate(country))
				System.out.println(Constants.DUPLICATEDE_MUNICIPALITY_MESSAGE);
			else
				System.out.println(Constants.MUNICIPALITY_INEXISTENT);
		} while(!country.equalsIgnoreCase(Constants.Q_MESSAGE) || district.getTerritories().isEmpty());

		System.out.print(Constants.SAVE_THE_CHANGES_MESSAGE);

		boolean notValid;
		do 
		{
			notValid = false;
			String temp = scanner.next();
			if(temp.equalsIgnoreCase(Constants.YES_MESSAGE)) 
			{
				districts.addDistrict(district);
				districts.write(Constants.DISTRICTS_FILEPATH);
				System.out.println(Constants.DISTRICT_VALID_MESSAGE);
			}
			else if (temp.equalsIgnoreCase(Constants.NO_MESSAGE))
				System.out.println(Constants.DISTRICT_NOT_SAVED_MESSAGE);
			else
			{
				System.out.print(Constants.INVALID_INPUT_MESSAGE);
				notValid = true;
			}
		} while (notValid);
	}

	private void insertNewHierarchy(Hierarchies hierarchies, ConversionElements conversionElements, Scanner scanner) throws IOException, CloneNotSupportedException
	{
		System.out.println();
		System.out.println(Printer.align(Constants.INSERT_NEW_HIERARCHY_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		NotLeafCategory root = createRoot(scanner, hierarchies);

		hierarchies.addHierarchy(root);
		populateTree(root, root, scanner);
		hierarchies.write(Constants.HIERARCHIES_FILEPATH);

		System.out.println(Constants.HIERARCHY_VALID_MESSAGE);

		Utility.clearConsole(Constants.TRANSACTION_TIME);

		conversionElements.update(hierarchies);
		conversionElements.initialize(hierarchies);
		if(conversionElements.getConversionElements().size() > 1) 
		{
			setConvFact(conversionElements, scanner);
			System.out.println(Printer.printConversionElements(conversionElements));
			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
		}

		conversionElements.write(Constants.CONVERSION_ELEMENTS_FILEPATH);
	}

	private void showDistricts(Districts districts) 
	{
		System.out.println();
		System.out.print(Printer.align(Constants.VIEW_ALL_DISTRICTS_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!districts.getDistricts().isEmpty())
		{
			System.out.println(Printer.columnize(Printer.printDistricts(districts), Constants.MENU_LINE_SIZE));
			System.out.println();
		}
		else
		{
			System.out.println(Constants.NO_DISTRICTS);
			System.out.println();
		}
	}

	private void showHierarchies(Hierarchies hierarchies, Scanner scanner) 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.VIEW_ALLA_HIERARCHIES_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!hierarchies.getLeaves().isEmpty()) 
		{
			System.out.println(Printer.printHierarchyRoots(hierarchies));
			int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_NUMBER_SELECT_HIERARCHY_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= hierarchies.getHierarchies().size()), scanner));

			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.TREE_PRINT_OF_HIERARCHY_MESSAGE, Constants.MENU_LINE_SIZE));
			System.out.println();
			System.out.println(Printer.printHierarchy(hierarchies.getHierarchies().get(index - 1),0));

			System.out.println();
			System.out.println(Printer.align(Constants.COMPLETE_PRINT_OF_HIERARCHY_MESSAGE, Constants.MENU_LINE_SIZE));

			System.out.println(Printer.printExtendHierarchy(hierarchies.getHierarchies().get(index - 1)));
			System.out.println();
		}
		else
		{	
			System.out.println(Constants.NO_HIERARCHIES);
			System.out.println();
		}
	}

	private void showConvFact(Hierarchies hierarchies, ConversionElements conversionElements, Scanner scanner) throws CloneNotSupportedException 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.VIEW_CONVERSION_FACTOR_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(conversionElements.getConversionElements().size() > 1) 
		{	
			System.out.println(Printer.printLeaves(hierarchies.differentiateLeaves()));

			StringBuilder listaFattori = new StringBuilder();
			int index = Integer.parseInt(Utility.checkCondition(Constants.SPECIFY_NAME_LEAF_CATEGORY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= hierarchies.getLeaves().size()), scanner));

			String name = hierarchies.differentiateLeaves().get(index - 1).getName();

			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.VIEW_CONVERSION_FACTOR_MESSAGE, Constants.MENU_LINE_SIZE));
			System.out.println();
			System.out.println(Constants.CONVERSION_FACTOR_ASSOCIATES_LEAF_CATEGORY_MESSAGE + name.toUpperCase());

			for(ConversionElement elem : conversionElements.getConversionElements())
				if(elem.getCouple().getFirstLeaf().getName().equals(name))
					listaFattori.append(Constants.SEPARATOR).append(Printer.printConversionElement(elem));

			System.out.println(listaFattori);
			System.out.println();
		}
		else
		{
			System.out.println(Constants.NO_CONV_FACT);
			System.out.println();
		}
	}

	private void showProposals(Hierarchies hierarchies, ExchangeProposals proposals, Scanner scanner) throws CloneNotSupportedException 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!hierarchies.getLeaves().isEmpty() && !proposals.getExchangeProposals().isEmpty()) 
		{
			System.out.println(Printer.printLeaves(hierarchies.differentiateLeaves()));

			StringBuilder proposalsList = new StringBuilder();
			int index = Integer.parseInt(Utility.checkCondition(Constants.SPECIFY_NAME_LEAF_CATEGORY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= hierarchies.getLeaves().size()), scanner));

			String name = hierarchies.differentiateLeaves().get(index - 1).getName();

			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
			System.out.println();
			System.out.println(Constants.PROPOSALS_ASSOCIATES_LEAF_CATEGORY_MESSAGE + name.toUpperCase());

			for(ExchangeProposal proposal : proposals.getExchangeProposals())
				if(proposal.getCouple().getFirstLeaf().getName().equals(name) || proposal.getCouple().getSecondLeaf().getName().equals(name))
					proposalsList.append(Printer.printExchangeProposalWithSatus(proposal));

			if(!proposalsList.isEmpty())
			{
				System.out.println(proposalsList);
				System.out.println();
			}
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}
		}
		else
		{
			System.out.println(Constants.NO_PROPOSALS);
			System.out.println();
		}
	}

	private void showClosedSets(ClosedSets closedSets)
	{
		System.out.println();
		System.out.println(Printer.align(Constants.SHOW_CLOSED_SETS, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!closedSets.getClosedSets().isEmpty())
			System.out.print(Printer.printClosedSets(closedSets));
		else
		{
			System.out.println(Constants.NO_CLOSED_SETS);
			System.out.println();
		}
	}

	private void populateTree(Category node, NotLeafCategory root, Scanner scanner)
	{
		if(node.hasChildren()) 
		{
			NotLeafCategory tmp = (NotLeafCategory) node;

			int index;
			do 
			{
				Utility.clearConsole(Constants.TRANSACTION_TIME);
				System.out.println();
				System.out.println(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
				System.out.println(Constants.CHOOSE_LEAF_CATEGORY_MESSAGE + Constants.NEW_LINE + Constants.CHOOSE_NOT_LEAF_CATEGORY_MESSAGE);
				System.out.println();
				System.out.println(Constants.CHOOSE_NO_SUBCATEGORY);
				System.out.print(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));

				index = Integer.parseInt(Utility.checkCondition(String.format(Constants.CHOOSE_LEAF_OPTIONS, node.getName()), Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 2), scanner));

				if(index == 1) 
				{
					LeafCategory leaf = createLeaf(root, scanner);
					tmp.addChildren(leaf);
				}
				else if (index == 2)
				{
					NotLeafCategory radice = createNotLeaf(root, scanner);
					tmp.getChildren().add(radice);
					populateTree(radice, root, scanner);
				}
				else if(tmp.getLeaves().isEmpty())
					System.out.println(Constants.NO_LEAVES);
			}
			while(index != 0 || tmp.getLeaves().isEmpty());
		}
	}

	private void setConvFact(ConversionElements conversionElements, Scanner scanner)
	{
		boolean notValid2;
		do 
		{
			System.out.println();
			System.out.println(Printer.align(Constants.INSERT_CONV_FACT, Constants.MENU_LINE_SIZE));
			System.out.println();

			notValid2 = false;

			try 
			{

				boolean notValid = false;

				System.out.println(Printer.printRemainingConversionFactor(conversionElements));
				//				devo controllare che:
				//				- il numero inserito sia effettivamente una coppia presente
				//				- il fattore di conversione sia valido (>0.5 && <2.0)
				//				- non faccia "sballare" la lista degli elementi di conversione
				int coupleSelected = Integer.parseInt(Utility.checkCondition(Constants.SPECIFY_COUPLE_NUMBER_MESSAGE, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner));
				if (coupleSelected > 0 && coupleSelected < conversionElements.getRemainingConversionElements().size() + 1) 
				{
					Couple couple = conversionElements.getRemainingConversionElements().get(coupleSelected - 1).getCouple();
					double [] x  = conversionElements.getConversionFactorRange(couple);
					System.out.printf(Constants.SPECIFY_CONVERSION_FACTOR_NUMBER_MESSAGE, x[0], x[1]);
					double fattoreIn;
					do 
					{
						try 
						{
							notValid = false;
							fattoreIn = Double.parseDouble(scanner.next());
							if(fattoreIn < x[0] || fattoreIn > x[1]) 
							{
								notValid = true;
								System.out.print(Constants.INVALID_INPUT_MESSAGE);
							}
							else 
							{
								conversionElements.replace(couple, fattoreIn);
								conversionElements.replace(new Couple(couple.getSecondLeaf(), couple.getFirstLeaf()), 1 / fattoreIn);
								conversionElements.automaticConvFactCalculate();
							}
						} 
						catch (NumberFormatException e) 
						{
							notValid = true;
							System.out.print(Constants.INVALID_INPUT_MESSAGE);
						}
					} while (notValid);
				}
				else 
				{
					notValid2 = true;
					System.out.println(Constants.INVALID_INPUT_MESSAGE_2);
				}
			}
			catch (NumberFormatException e) 
			{
				notValid2 = true;
				System.out.println(Constants.INVALID_INPUT_MESSAGE);
			}
			Utility.clearConsole(Constants.TRANSACTION_TIME);
		} while(conversionElements.isFactConvPresent(0.) || notValid2);
	}

	private LeafCategory createLeaf(Category root, Scanner scanner)
	{
		String name = Utility.check2Condition(Constants.INSERT_CATEGORY_NAME_MASSAGE, Constants.INVALID_INPUT_MESSAGE, Constants.CATEGORY_ALREADY_INSERT, String::isBlank, root::contains, scanner);
		String nameDomain = Utility.checkCondition(Constants.SPECIFY_DOMAIN_NAME_MESSAGE, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);
		String yesOrNo = Utility.checkCondition(Constants.DESCRIPTION_ASSOCIATED_WITH_DOMAIN_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !input.equalsIgnoreCase(Constants.YES_MESSAGE) && !input.equalsIgnoreCase(Constants.NO_MESSAGE), scanner);
		String description = null;

		if(yesOrNo.equalsIgnoreCase(Constants.YES_MESSAGE)) 
		{
			System.out.print(Constants.INSERT_CATEGORY_DESCRIPTION_MESSAGE);
			description = Utility.checkCondition(Constants.INSERT_CATEGORY_DESCRIPTION_MESSAGE, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);
		}

		return new LeafCategory(name, nameDomain, description);
	}

	private NotLeafCategory createNotLeaf(Category root, Scanner scanner) 
	{
		String name = Utility.check2Condition(Constants.INSERT_CATEGORY_NAME_MASSAGE, Constants.INVALID_INPUT_MESSAGE, Constants.CATEGORY_ALREADY_INSERT, String::isBlank, root::contains, scanner);
		String nameDomain = Utility.checkCondition(Constants.SPECIFY_DOMAIN_NAME_MESSAGE, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);
		String yesOrNo = Utility.checkCondition(Constants.DESCRIPTION_ASSOCIATED_WITH_DOMAIN_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !input.equalsIgnoreCase(Constants.YES_MESSAGE) && !input.equalsIgnoreCase(Constants.NO_MESSAGE), scanner);
		String description = null;

		if(yesOrNo.equalsIgnoreCase(Constants.YES_MESSAGE)) 
		{
			System.out.print(Constants.INSERT_CATEGORY_DESCRIPTION_MESSAGE);
			description = scanner.next();
		}
		String nameField = Utility.checkCondition(Constants.SPECIFY_FIELD_NAME, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);

		return new NotLeafCategory(name, nameDomain, description, nameField);
	}

	private NotLeafCategory createRoot(Scanner scanner, Hierarchies hierarchies) 
	{
		String name = Utility.check2Condition(Constants.INSERT_ROOT_NAME_MESSAGE, Constants.INVALID_INPUT_MESSAGE, Constants.ROOT_ALREADY_EXIST_MESSAGE, String::isBlank, hierarchies::containsRoot, scanner);
		String field = Utility.checkCondition(Constants.SPECIFY_FIELD_NAME, Constants.INVALID_INPUT_MESSAGE, String::isBlank, scanner);

		return new NotLeafCategory(name, null, null, field);
	}
}