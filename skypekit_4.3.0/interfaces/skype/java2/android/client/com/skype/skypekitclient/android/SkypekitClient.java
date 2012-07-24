/**
 * Copyright (C) 2010, Skype Limited
 *
 * All intellectual property rights, including but not limited to copyrights,
 * trademarks and patents, as well as know how and trade secrets contained in,
 * relating to, or arising from the internet telephony software of
 * Skype Limited (including its affiliates, "Skype"), including without
 * limitation this source code, Skype API and related material of such
 * software proprietary to Skype and/or its licensors ("IP Rights") are and
 * shall remain the exclusive property of Skype and/or its licensors.
 * The recipient hereby acknowledges and agrees that any unauthorized use of
 * the IP Rights is a violation of intellectual property laws.
 *
 * Skype reserves all rights and may take legal action against infringers of
 * IP Rights.
 *
 * The recipient agrees not to remove, obscure, make illegible or alter any
 * notices or indications of the IP Rights and/or Skype's rights and
 * ownership thereof.
 */

package com.skype.skypekitclient.android;

import java.io.IOException;

/**
 *
 */
public class SkypekitClient extends com.skype.skypekitclient.SkypekitClient
{
    protected void usage(String message)
    {
        if (!message.isEmpty())
            error(message);

        System.err.println(
                "Usage: " + TAG + " [CONNECTION][OPTIONS]\n\n"
                + "CONNECTION:\n"
                + "either tcp\n"
                + "\t-i ip addr \t\t- ip address (default " + inetAddr + ").\n"
                + "\t-p port \t\t- tcp port port (default " + portNum + ").\n"
                + "or local\n"
                + "\t-l local_connection_name \n"
                + "OPTIONS:\n"
                + "\t-t app_token_filename \t- file must contain a valid application token.\n"
                + "\t-r log_file_base \t- record transport streams in log path. \"-r ./transport\" will produce ./transport_log_in.1 and  ./transport_log_out.1\n"
                + "\t-a \t- dispatch all event (don't filter on object presence\n");

        System.exit(1);
    }

    protected com.skype.ipc.ClientConfiguration configure(String[] args, com.skype.ipc.ClientConfiguration configuration)
    {
        String localName = null;
        ClientConfiguration cfg = (ClientConfiguration) configuration;

        for (int i = 0, numArgs = args.length; i < numArgs; i++) {
            if ((args[i].charAt(0) == '-') || (args[i].charAt(0) == '/')) {
                switch (args[i].charAt(1)) {
                case 'i': { // internet address
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        inetAddr = args[++i].toString();
                    }
                    break;
                }
                case 'p': { // port number
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        portNum = Integer.parseInt(args[++i]);
                    }
                    break;
                }
                case 'l': { // local connection?
                  if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                      usage("malformed argument list");
                  }
                  else {
                      localName = args[++i].toString();
                  }
                    break;
                }
                case 'r': { // log transport streams?
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        configuration.generateTransportLog(args[++i].toString());
                    }
                    break;
                }
                case 't': { // token
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        pemFileName = configuration.setCertificate(args[++i].toString());
                    }
                    break;
                }
                case 'n': { // internal - no-TLS
                    internal = true;
                    configuration.dontUseTls();
                    break;
                }
                case 'd': { // internal - enable debug
                    Log.level = Log.Severity.kDebug;
                    break;
                }
                case 'a':
                    configuration.setDispatchAll();
                    break;
                default:
                    if (strict)
                        usage("Unsupported argument found:" + args[i]);
                    break;
                }
            }
            else {
                usage("malformed argument list");
                break;
            }
        }
        if (pemFileName.isEmpty()) {
            usage("-t Certificate file path argument missing.");
        }

        if (localName != null)
            cfg.setLocalTransportName(localName);
        else
            configuration.setTcpTransport(inetAddr, portNum);

        return configuration;
    }

    /**
     * Java command line client for SkypeKit
     * 
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        // System.setProperty("javax.net.debug","all");
        new SkypekitClient(args, new ClientConfiguration());
    }
}

