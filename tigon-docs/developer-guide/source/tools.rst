.. :author: Cask Data, Inc.
   :description: Command-line interface
   :copyright: Copyright © 2014 Cask Data, Inc.

============================================
Tigon Tools
============================================

Tigon Command-Line Interface
============================

Introduction
------------

The Command-Line Interface (CLI) provides methods to interact with a running Tigon
instance from within a shell, similar to HBase shell or ``bash``. It is automatically
started once you start Tigon in `Distributed Mode </admin#distributed-mode>`__.

.. highlight:: console

The executable should bring you into a shell, with this prompt::

  tigon >

To list all of the available commands, enter ``help``::

  tigon > help

To list all Flows currently running in Tigon, use::

  tigon > list
  
To stop the Flow prior to shutting down distributed mode, use::

  tigon > delete <flow-name>

You can then exit the command-line interface either with ``quit`` or ``Control-C``.

Available Commands
------------------

These are the available commands:

.. csv-table::
   :header: Command,Description
   :widths: 50, 50

   **General**
   ``help``,Prints this helper text
   ``version``,Prints the version
   ``status``,Prints the current Tigon system status
   ``quit``,Quits the shell

   **Starting Elements**
   ``start <path-to-jar> <flow-classname>``,Starts a Flow
   ``stop <flow-name>``,Stops a Flow *flow-name*
   ``delete <flow-name>``,Stops and Deletes the Queues for a Flow *flow-name*
   ``set <flow-name> <flowlet-name> <instances>``,Set the number of instance of a Flowlet *flowlet-name* for a Flow *flow-name*

   **Listing Elements**
   ``list``,Lists all Flows which are currently running
   ``serviceinfo <flow-name>``,Prints all Services announced in a Flow *flow-name*
   ``discover <flow-name> <service-name>``,Discovers a service endpoint of a Service *service-name* for a Flow *flow-name*
   ``flowletinfo <flow-name>``,Prints Flowlet Names and corresponding Instances for a Flow *flow-name*
   ``showlogs <flow-name>``,Shows live logs for a Flow *flow-name*


Where to Go Next
================

Now that you're seen the command line tool for Tigon, take a look at:

- `Admin <admin.html>`__, which describes administrating a Tigon application.
