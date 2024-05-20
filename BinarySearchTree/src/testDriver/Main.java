package testDriver;

import java.util.Scanner;

import ch07.trees.BinarySearchTree;

public class Main {   
public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    BinarySearchTree<Integer> binarySearch = new BinarySearchTree<>();

    int choice;
    String displayMenu = "\nMENU:"
    		+ "\n1. Add int "
    		+ "\n2. Display count of values less than or equal to argument value"
    		+ "\n3. Display count of values greater than or equal to argument value"
    		+ "\n4. Display height"
    		+ "\n5. Balance tree"
    		+ "\n6. Print binary tree using breadth-first order traversal"
    		+ "\n7. Call fratio (Will balance tree if less than 0.5)"
    		+ "\n8. End Program";
    do {
    System.out.println(displayMenu);

        System.out.print("Enter your choice: ");
        choice = input.nextInt();
        
        
        switch (choice) {
            case 1:
                System.out.print("Enter int: ");
                int addInt = input.nextInt();
                binarySearch.add(addInt);
                break;
            case 2:
                System.out.print("Enter number: ");
                int lessThanNum = input.nextInt();
                System.out.println("Number of ints less than or equal to " + lessThanNum + ": " +
                        binarySearch.countLesser(lessThanNum));
                break;
            case 3:
                System.out.print("Enter value: ");
                int greaterThanNum = input.nextInt();
                System.out.println("Number of ints greater than or equal to " + greaterThanNum + ": " +
                       binarySearch.countGreater(greaterThanNum));
                break;
            case 4:
                System.out.println("The tree height is: " + binarySearch.height());    
                   break;
            case 5:
            	binarySearch.balanceTree();
            	if (binarySearch.fRatio() > 0.5) {
            		System.out.println("Your tree is already balanced!");
            	} else {
            	System.out.println("Your tree has been balanced");
            	}
            	break;
            case 6:
                System.out.println("Breadth-First Traversal:");
                binarySearch.breadthFirstTraversal();
                break;
            case 7:
                double fRatio = binarySearch.fRatio();
                System.out.println("Fullness Ratio: " + fRatio);
                if (fRatio < 0.5) {
                    System.out.println("Your tree's ratios is less than 0.5, balancing tree...");
                    binarySearch.balanceTree();
                    System.out.println("Tree after balancing:");
                    binarySearch.breadthFirstTraversal();
                }
                break;
            case 8:
                System.out.println("Exiting, Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please enter a choice from the menu.");
        }
    } while (choice != 8);
    input.close();
        }
    }
