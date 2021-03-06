package com.google.sampling.experiential.server.migration;

import java.util.Map;
import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.sampling.experiential.server.migration.jobs.ExperimentJDOToDatastoreMigration;
import com.google.sampling.experiential.server.migration.jobs.FeedbackTypeRepairMigration;
import com.google.sampling.experiential.server.migration.jobs.TestJDODSCompat;

public class MigrationLookupTable {
  private static final Logger log = Logger.getLogger(MigrationLookupTable.class.getName());


  private static Map<String, Class> migrations = Maps.newHashMap();
  static {
    migrations.put("96", ExperimentJDOToDatastoreMigration.class);
    migrations.put("97", FeedbackTypeRepairMigration.class);
    migrations.put("965", TestJDODSCompat.class);
  }
  public static MigrationJob getMigrationByName(String name) {
    if (Strings.isNullOrEmpty(name)) {
      log.info("Could not run migration - no jobName specified");
      return null;
    }
    Class migrationClass = migrations.get(name);
    if (migrationClass != null) {
      try {
        return (MigrationJob) migrationClass.newInstance();
      } catch (InstantiationException e) {
        log.severe("Could not instantiate migration named: " + name);
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        log.severe("Did not have access to instantiate migration named: " + name);
        e.printStackTrace();
      }
    } else {
      log.info("Migration name " + name + " does not exist in map");
    }
    return null;
  }


}
