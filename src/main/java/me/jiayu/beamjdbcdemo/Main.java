package me.jiayu.beamjdbcdemo;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.BigEndianIntegerCoder;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;

public class Main {

  public static void main(String[] args) {
    PipelineOptionsFactory.register(JdbcDemoPipelineOptions.class);
    JdbcDemoPipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(JdbcDemoPipelineOptions.class);

    Pipeline p = Pipeline.create(options);

    JdbcIO.DataSourceConfiguration config =
        JdbcIO.DataSourceConfiguration.create("org.sqlite.JDBC", options.getSqliteFileName());

    var selected =
        p.apply(
            JdbcIO.<KV<Integer, People>>read()
                .withDataSourceConfiguration(config)
                .withQuery("select id, name, age from person")
                .withCoder(KvCoder.of(BigEndianIntegerCoder.of(), AvroCoder.of(People.class)))
                .withRowMapper(
                    (JdbcIO.RowMapper<KV<Integer, People>>)
                        resultSet -> {
                          var name = resultSet.getString("name");
                          var id = resultSet.getInt("id");
                          var age = resultSet.getInt("age");
                          return KV.of(id, new People(id, name, age));
                        }));

    selected.apply(
        JdbcIO.<KV<Integer, People>>writeVoid()
            .withDataSourceConfiguration(config)
            .withStatement("insert into person_view (id, name, age) values (?, ?, ?)")
            .withPreparedStatementSetter(
                (JdbcIO.PreparedStatementSetter<KV<Integer, People>>)
                    (element, preparedStatement) -> {
                      People p1 = element.getValue();
                      preparedStatement.setInt(1, p1.getId());
                      preparedStatement.setString(2, p1.getName());
                      preparedStatement.setInt(3, p1.getAge() + 100);
                    }));

    var runResult = p.run();
    var state = runResult.waitUntilFinish();
    System.out.printf("run result %s", state);
  }
}
