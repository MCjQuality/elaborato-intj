package it.unibs.ing.elaborato;

import java.io.IOException;
import java.util.Scanner;

public class MenuConsumer {

	private static String createMenuConsumer() 
	{
		StringBuilder menuBuffer = new StringBuilder();

		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Utility.appendHorizontalLine(Constants.MENU_LINE_SIZE));
		menuBuffer.append(Constants.NEW_LINE);
		menuBuffer.append(Printer.align(Constants.MENU_MESSAGE, Constants.MENU_LINE_SIZE));
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

	public boolean menuConsumerManager(Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws IOException
	{
		int index;

		do 
		{
			System.out.print(createMenuConsumer());
			index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_0_MESSAGE && Integer.parseInt(input) <= Constants.NUMBER_5_MESSAGE), scanner));
			chooseOptionMenu(index, hierarchies, scanner, consumer, conversionElements, exchangeProposals, closedSets);
		} while(index != Constants.NUMBER_5_MESSAGE && index != Constants.NUMBER_0_MESSAGE);

		return index == Constants.NUMBER_5_MESSAGE;
	}

	private void chooseOptionMenu(int choice, Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws IOException
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
			System.out.println(Constants.LOGOUT_MESSAGE);
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
		System.out.println(Printer.align(Constants.NAVIGATE_THROUGH_HIERARCHIES_MESSAGE, Constants.MENU_LINE_SIZE));
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

		int i = Constants.NUMBER_1_MESSAGE;
		for(NotLeafCategory node : hierarchies.getHierarchies())
			result.append(i++).append(Constants.SEPARATOR).append(node.getName()).append(Constants.NEW_LINE);

		System.out.println(result);

		int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_THE_NUMBER_RELATED_TO_THE_HIERARCHY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_1_MESSAGE && Integer.parseInt(input) <= hierarchies.getHierarchies().size()), scanner));
		String node_name = hierarchies.getHierarchies().get(index - Constants.NUMBER_1_MESSAGE).getName();

        return hierarchies.getHierarchies()
				.stream()
				.filter(x -> x.getName().equals(node_name))
				.toList()
				.getFirst();
	}

	private void navigateHierarchy(Category father, Scanner scanner)
	{
		if(father.hasChildren())
		{
			System.out.println();
			System.out.printf((Constants.ASSOCIATED_NODE_TO_DOMAIN) + Constants.NEW_LINE, father.getName());

			Utility.clearConsole(Constants.READING_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.NAVIGATE_THROUGH_HIERARCHIES_MESSAGE, Constants.MENU_LINE_SIZE));
			System.out.println();

			StringBuffer result = new StringBuffer();
			String node_name;
			int i = Constants.NUMBER_1_MESSAGE;

			System.out.println(((NotLeafCategory) father).getField() + Constants.COLONS);
			for(Category node : father.getChildren())
				result.append(i++).append(Constants.SEPARATOR).append(node.getDomain()).append(Constants.NEW_LINE);

			System.out.println(result);

			int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_THE_NUMBER_RELATED_TO_THE_HIERARCHY, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_1_MESSAGE && Integer.parseInt(input) <= father.getChildren().size()), scanner));
			node_name = father.getChildren().get(index - Constants.NUMBER_1_MESSAGE).getName();

			Category node = father.getChildren()
					.stream()
					.filter(x -> x.getName().equals(node_name))
					.toList()
					.getFirst();

			navigateHierarchy(node, scanner);
		}
		else
		{
			System.out.println();
			System.out.printf((Constants.ASSOCIATED_NODE_TO_DOMAIN), Constants.LIGHT_BLUE_FORMAT + father.getName() + Constants.RESET_FORMAT);
		}
	}

	private void formulateProposal(Hierarchies hierarchies, Scanner scanner, Consumer consumer, ConversionElements conversionElements, ExchangeProposals exchangeProposals, ClosedSets closedSets) throws IOException
	{
		System.out.println();
		System.out.println(Printer.align(Constants.ADD_EXCHANGE_PROPOSAL_MENU_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!hierarchies.getLeaves().isEmpty())
		{

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
		int hours = Integer.parseInt(Utility.checkCondition(Constants.HOURS_REQUESTED,Constants.INVALID_INPUT_MESSAGE , input->input.isBlank() || !Utility.isInt(input) || Integer.parseInt(input) <= 0, scanner));

		return new ExchangeProposal(couple, hours, consumer, conversionElements);
	}

	private void withdrawnProposal(Scanner scanner, Consumer consumer, ExchangeProposals exchangeProposals) throws IOException
	{
		System.out.println();
		System.out.println(Printer.align(Constants.WITHDRAW_PROPOSAL_MESSAGE, Constants.MENU_LINE_SIZE));
		System.out.println();

		if(!exchangeProposals.activeProposalBelongOneConsumer(consumer).isEmpty()) 
		{
			System.out.printf(Constants.ACTIVE_PROPOSALS_RELATED_TO_A_CONSUMER, consumer.getUsername());
			System.out.println(Printer.printNumberedExchangeProposals(exchangeProposals.activeProposalBelongOneConsumer(consumer)));

			int index = Integer.parseInt(Utility.checkCondition(Constants.INSERT_PROPOSAL_NUMBER, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_0_MESSAGE && Integer.parseInt(input) <= exchangeProposals.activeProposalBelongOneConsumer(consumer).size()), scanner));

			if(index != Constants.NUMBER_0_MESSAGE)
			{
				System.out.println();
				System.out.println(Constants.WITHDRAWN_PROPOSAL);
				ExchangeProposal elem = exchangeProposals.activeProposalBelongOneConsumer(consumer).get(index - Constants.NUMBER_1_MESSAGE);
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
		System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
		System.out.println();
		System.out.println(Constants.SHOW_OPEN_PROPOSAL);
		System.out.println(Constants.SHOW_CLOSED_PROPOSAL);
		System.out.println(Constants.SHOW_WITHDRAWN_PROPOSAL);
		System.out.println();

		int index = Integer.parseInt(Utility.checkCondition(Constants.SELECT_FROM_THE_OPTIONS_MESSAGE, Constants.INVALID_INPUT_MESSAGE, input -> !Utility.isInt(input) || !(Integer.parseInt(input) >= Constants.NUMBER_1_MESSAGE && Integer.parseInt(input) <= Constants.NUMBER_3_MESSAGE), scanner));

		switch (index) 
		{
		case Constants.NUMBER_1_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
			System.out.println();

			if(!exchangeProposals.activeProposalBelongOneConsumer(consumer).isEmpty())
				System.out.print(Printer.printExchangeProposals(exchangeProposals.activeProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.nextLine();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		case Constants.NUMBER_2_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
			System.out.println();

			if(!exchangeProposals.closedProposalBelongOneConsumer(consumer).isEmpty())
				System.out.println(Printer.printExchangeProposals(exchangeProposals.closedProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.nextLine();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		case Constants.NUMBER_3_MESSAGE: 
		{
			Utility.clearConsole(Constants.TRANSACTION_TIME);

			System.out.println();
			System.out.println(Printer.align(Constants.SHOW_PROPOSAL, Constants.MENU_LINE_SIZE));
			System.out.println();			

			if(!exchangeProposals.withdrawnProposalBelongOneConsumer(consumer).isEmpty())
				System.out.println(Printer.printExchangeProposals(exchangeProposals.withdrawnProposalBelongOneConsumer(consumer)));
			else
			{
				System.out.println(Constants.NO_PROPOSALS);
				System.out.println();
			}

			System.out.print(Constants.PRESS_ANY_BUTTONS_TO_GO_BACK);
			scanner.nextLine();
			Utility.clearConsole(Constants.TRANSACTION_TIME);
			break;
		}
		default:
			System.out.println(Constants.INSERT_VALID_OPTION_MENU_MESSAGE);
		}
	}

}
