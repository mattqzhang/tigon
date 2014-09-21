/*
 * Copyright © 2014 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.tigon.sql.flowlet;

/**
 * Basic InputFlowlet for Testing.
 */
public class InputFlowletBasic extends AbstractInputFlowlet {

  @Override
  public void create() {
    setName("summation");
    setDescription("sums up the input value over a timewindow");
    StreamSchema schema = new StreamSchema.Builder()
      .addField("timestamp", GDATFieldType.LONG, GDATSlidingWindowAttribute.INCREASING)
      .addField("intStream", GDATFieldType.INT)
      .build();
    addJSONInput("intInput", schema);
    addQuery("sumOut", "SELECT timestamp, SUM(intStream) AS sumValue FROM intInput GROUP BY timestamp");
  }
}
