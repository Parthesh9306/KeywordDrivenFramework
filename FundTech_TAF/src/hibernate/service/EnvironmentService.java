package hibernate.service;

import hibernate.entity.Environment;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * perform operation related to group result
 * 
 * @author Chetan.Aher
 * 
 */
public class EnvironmentService {

    public EnvironmentService() {
        HibernateUtilService.getInstance();
    }

    /**
     * Find Environment by id
     * 
     * @param environmentId
     * @return
     */
    public Environment findById(Integer environmentId) {
        Environment environment = null;

        if (environmentId != null) {
            Session session = HibernateUtilService.getSessionFactory()
                .openSession();
            Transaction transation = session.beginTransaction();

            environment = (Environment) session.get(Environment.class,
                environmentId);

            transation.commit();
            session.close();
        }

        return environment;
    }

    /**
     * Create environment file dynamically
     * 
     * @param environmentId
     * @throws IOException
     */
    /*public void createEnvironment(Integer environmentId) throws IOException {
        Environment environment = findById(environmentId);

        if (null != environment) {
            BufferedWriter bufferedWriter = null;
            File environmentFile = new File(Debug.objectsFolderPath
                + environment.getName() + ".properties");

            if (!environmentFile.exists()) {
                environmentFile.createNewFile();
            }

            Debug.enviromentIsOn = true;
            Debug.enviromentFileName = environment.getName();

            Writer writer = new FileWriter(environmentFile);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(environment.getValue());
            bufferedWriter.close();
        }
    }*/

}
