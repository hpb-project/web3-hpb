package com.hpb.web3.console;

import com.hpb.web3.codegen.Console;

import static com.hpb.web3.utils.Collection.tail;


public class WalletRunner {
    private static final String USAGE = "wallet create|update|send|fromkey";

    public static void run(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "create":
                    WalletCreator.main(new String[] {});
                    break;
                case "update":
                    WalletUpdater.main(tail(args));
                    break;
                case "send":
                    WalletSendFunds.main(tail(args));
                    break;
                case "fromkey":
                    KeyImporter.main(tail(args));
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}
