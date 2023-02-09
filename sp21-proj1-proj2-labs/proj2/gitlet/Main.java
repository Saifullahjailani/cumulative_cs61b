package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0 ) {
                throw new GitletException("Please enter a command.");

            }
            String firstArg = args[0];


            switch (firstArg) {
                case "init":
                    Repository.init();
                    break;
                case "add":
                    Staging.add(args[1]);
                    break;
                case "log":
                    Repository.log(Repository.currentBranch().getHead());
                    break;
                case "rm":
                    Repository.rm(args[1]);
                    break;
                case "status":
                    if (!Repository.GITLET_DIR.exists()) {
                        throw new GitletException("Not in an initialized Gitlet directory.");
                    }
                    Repository.status();
                    break;
                case "find":
                    Repository.find(args[1]);
                    break;
                case "branch":
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    Repository.rmBranch(args[1]);
                    break;
                case "commit":
                    Commit.commit(args[1]);
                    break;
                case "global-log":
                    Repository.globalLog();
                    break;
                case "reset":
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    Repository.merge(args[1]);
                    break;
                case "checkout":
                    processCheckout(args);
                    break;
                case "longUid":
                    System.out.println(Repository.getFullCommitID(args[1]));
                    break;
                case "getSplit":
                    System.out.println(Repository.currentBranch().getSplitPoint(Branch.getBranch(args[1])).getId());
                    break;
                default:
                    throwError();

            }
        } catch (GitletException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void throwError() {
        throw new GitletException("No command with that name exists.");
    }

    private static void throwError(String message) {
        throw new GitletException(message);
    }

    private static void processCheckout(String[] args) {
        int len = args.length;
        if (len == 2 && !args[1].isEmpty()) {
            Repository.checkout(args[1]);

        } else if (len == 3 && args[1].equals("--")) {
            Repository.checkout(args[2], Repository.getHead());
        } else if (len == 4 && args[2].equals("--")) {
            Repository.checkout(args[3], args[1]);
        } else {
            throwError("Incorrect operands.");
        }

    }
}
