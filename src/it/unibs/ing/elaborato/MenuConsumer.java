package it.unibs.ing.elaborato;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MenuConsumer {

	private static String createMenuConsumer() 
	{
		StringBuffer menuBuffer = new StringBuffer();

		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Printer.align(Constants.MENU_MESSAGE, 56));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_1_MENU_CONSUMER_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_2_CONSUMER_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_3_CONSUMER_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_4_MENU_CONSUMER_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Constants.OPTION_5_MENU_CONSUMER_MESSAGE);
		menuBuffer.append(Constants.DOUBLE_NEW_LINE);
		menuBuffer.append(Constants.OPTION_0_MENU_MESSAGE);
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);

		return menuBuffer.toString();
	}

	public boolean menuConsumerManager(Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws FileNotFoundException, IOException 
	{
		int index = Constants.NUMBER_0_MESSAGE;

		do 
		{
			System.out.print(createMenuConsumer());
			index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 5), scanner));
			chooseOptionMenu(index, hierarchies, scanner, consumer, conversionElements, exchangeProposals, closedSets);
		} while(index != Constants.NUMBER_5_MESSAGE && index != Constants.NUMBER_0_MESSAGE);

		return index == 5 ? true : false; 
	}

	private void chooseOptionMenu(int choice, Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws FileNotFoundException, IOException 
	{
		switch (choice)
		{
		case Constants.NUMBER_1_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			navigate(hierarchies, scanner);
			break;
		case Constants.NUMBER_2_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			formulateProposal(hierarchies, scanner, consumer, conversionElements, exchangeProposals, closedSets);
			break;
		case Constants.NUMBER_3_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			withdrawnProposal(scanner, consumer, exchangeProposals);
			break;
		case Constants.NUMBER_4_MESSAGE:
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			showProposals(scanner, consumer, exchangeProposals);
			break;
		case Constants.NUMBER_5_MESSAGE:
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

	private void navigate(Hierarchies hierarchies, Scanner scanner) 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.NAVIGATE_THROUGH_HIERARCHIES_MESSAGE, 56));
		System.out.println();

		if(!hierarchies.getHierarchies().isEmpty()) 
		{
			navigateHierarchy(navigateRoots(hierarchies, scanner), scanner);
			Utility.clearConsole(Constants.READING_TIME);
		}
		else 
		{
			System.out.println(Constants.NO_HIERARCHY);
			Utility.clearConsole(Constants.READING_TIME);
		}
	}

	private NotLeafCategory navigateRoots(Hierarchies hierarchies, Scanner scanner) 
	{
		StringBuffer result = new StringBuffer();

		int i = 1;
		for(NotLeafCategory node : hierarchies.getHierarchies()) 
			result.append(i++ + Constants.SEPARATOR + node.getName() + Constants.NEW_LINE);

		System.out.println(result.toString());

		int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_THE_NUMBER_RELATED_TO_THE_HIERARCHY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= hierarchies.getHierarchies().size()), scanner));
		String node_name = hierarchies.getHierarchies().get(index - 1).getName();

		NotLeafCategory node = hierarchies.getHierarchies()
				.stream()
				.filter(x -> x.getName().equals(node_name))
				.toList()
				.get(0);

		return node;
	}

	private Category navigateHierarchy(Category father, Scanner scanner) 
	{
		System.out.println();
		System.out.println(String.format(Constants.ASSOCIATED_NODE_TO_DOMAIN, father.getName()));
		System.out.println();

		if(father.hasChildren()) 
		{
			StringBuffer result = new StringBuffer();
			String node_name;
			int i = 1;

			for(Category node : father.getChildren()) 
			{
				result.append(i++ + Constants.SEPARATOR);
				if(!node.hasChildren())
					result.append(Constants.LIGHT_BLUE_FORMAT + node.getName() + Constants.RESET_FORMAT);
				else
					result.append(node.getName());
				result.append(Constants.NEW_LINE);
			}

			System.out.println(result.toString());

			int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_THE_NUMBER_RELATED_TO_THE_HIERARCHY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= father.getChildren().size()), scanner));

			node_name = father.getChildren().get(index - 1).getName();

			Category node = father.getChildren()
					.stream()
					.filter(x -> x.getName().equals(node_name))
					.toList()
					.get(0);

			navigateHierarchy(node, scanner);
		}
		return father;
	}

	private void formulateProposal(Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws FileNotFoundException, IOException 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.ADD_EXCHANGE_PROPOSAL_MENU_MESSAGE, 56));
		System.out.println();

		if(!hierarchies.getLeaves().isEmpty()) {

			ExchangeProposal exchangeProposal = createExchangeProposal(hierarchies, scanner, consumer, conversionElements);

			if(!exchangeProposals.contains(exchangeProposal))
			{
				System.out.println();
				System.out.println(Printer.printExchangeProposal(exchangeProposal));
				String yesOrNo = Utility.checkCondition(Constants.SAVING_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !input.equals(Constants.YES_MESSAGE) && !input.equals(Constants.NO_MESSAGE), scanner);
				if(yesOrNo.equals(Constants.YES_MESSAGE)) 
				{
					exchangeProposals.add(exchangeProposal);
					exchangeProposals.write(Constants.PROPOSALS_FILEPATH);
					System.out.println(Constants.PROPOSAL_SAVED_MESSAGE);
					ExchangeProposals closedSet = new ExchangeProposals();
					if(exchangeProposals.verifyClosedProposals(exchangeProposal, exchangeProposal, closedSet)) 
					{		
						System.out.println(Constants.EXCHANGE_PROPOSAL_CLOSED_MESSAGE);
						closedSets.add(closedSet);
						closedSets.write(Constants.CLOSED_SETS_FILEPATH);
					}
					Utility.clearConsole(Constants.READING_TIME);
				}
				else
				{
					System.out.println(Constants.PROPOSAL_NOT_SAVED_MESSAGE);
					Utility.clearConsole(Constants.READING_TIME);
				}
			}
			else 
			{
				System.out.println(Constants.PROPOSAL_ALREADY_INSERT);
				Utility.clearConsole(Constants.READING_TIME);
			}
		}
		else 
		{
			System.out.print(Constants.NO_LEAVES);
			Utility.clearConsole(Constants.READING_TIME);
		}
	}

	private ExchangeProposal createExchangeProposal(Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements)
	{
		String request = Utility.check2Condition(Constants.SERVICE_REQUESTED, Constants.INVALID_INPUT_MESSAGE, Constants.LEAF_CATEGORY_DOES_NOT_EXIST , String::isBlank, input -> !hierarchies.isLeafPresent(input), scanner);
		String offer = Utility.check2Condition(Constants.SERVICE_OFFERED, Constants.INVALID_INPUT_MESSAGE, Constants.LEAF_CATEGORY_DOES_NOT_EXIST , input -> input.isBlank() || input.equals(request), input -> !hierarchies.isLeafPresent(input), scanner);
		Couple couple = new Couple(hierarchies.findLeaf(request), hierarchies.findLeaf(offer));
		int hours = Integer.parseInt(Utility.checkCondition(Constants.HOURS_REQUESTED,Constants.INVALID_INPUT_MESSAGE , String::isBlank, scanner));

		return new ExchangeProposal(couple, hours, consumer, conversionElements);
	}

	private void withdrawnProposal(Scanner scanner, Consumer consumer, ExchangeProposals exchangeProposals) throws FileNotFoundException, IOException 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.WITHDRAW_PROPOSAL_MESSAGE, 56));
		System.out.println();

		if(!exchangeProposals.activeProposalBelongOneConsumer(consumer).isEmpty()) 
		{
			System.out.printf(Constants.ACTIVE_PROPOSALS_RELATED_TO_A_CONSUMER, consumer.getUsername());
			System.out.println(Printer.printNumberedExchangeProposals(exchangeProposals.activeProposalBelongOneConsumer(consumer)));

			int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_PROPOSAL_NUMBER, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= exchangeProposals.activeProposalBelongOneConsumer(consumer).size()), scanner));

			if(index != 0) 
			{
				System.out.println();
				System.out.println(Constants.WITHDRAWN_PROPOSAL);
				ExchangeProposal elem = exchangeProposals.activeProposalBelongOneConsumer(consumer).get(index - 1);
				System.out.println(Printer.printExchangeProposal(elem));
				elem.changeStatusToWithdrawn();
				exchangeProposals.write(Constants.PROPOSALS_FILEPATH);
				System.out.println(Constants.PROPOSAL_SUCCESSFULLY_WITHDRAWN);
			}
		}
		else 
			System.out.println(Constants.NO_PROPOSALS);
		Utility.clearConsole(Constants.READING_TIME);
	}

	private void showProposals(Scanner scanner, Consumer consumer, ExchangeProposals exchangeProposals) 
	{
		System.out.println();
		System.out.println(Printer.align(Constants.SHOW_PROPOSAL, 56));
		System.out.println();
		System.out.println(Constants.SHOW_OPEN_PROPOSAL);
		System.out.println(Constants.SHOW_CLOSED_PROPOSAL);
		System.out.println(Constants.SHOW_WITHDRAWN_PROPOSAL);
		System.out.println();

		int index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 3), scanner));

		switch (index) 
		{
		case Constants.NUMBER_1_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, 56));
			System.out.println();

			if(!exchangeProposals.activeProposalBelongOneConsumer(consumer).isEmpty())
				System.out.print(Printer.printExchangeProposals(exchangeProposals.activeProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		case Constants.NUMBER_2_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, 56));
			System.out.println();

			if(!exchangeProposals.closedProposalBelongOneConsumer(consumer).isEmpty())
				System.out.println(Printer.printExchangeProposals(exchangeProposals.closedProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		case Constants.NUMBER_3_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, 56));
			System.out.println();			

			if(!exchangeProposals.withdrawnProposalBelongOneConsumer(consumer).isEmpty())
				System.out.println(Printer.printExchangeProposals(exchangeProposals.withdrawnProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.next();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		default:
			System.out.println(Constants.INSERT_VALID_OPTION_MENU_MESSAGE);
		}
	}

}
