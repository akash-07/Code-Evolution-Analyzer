package tryDistiller;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by @kash on 3/9/2018.
 */
public class tryJGit {
    public static void main(String[] args)  {
        File directory = new File("F:/Study-Ebooks/March-2018/Paper/.git");
        try{
            //Initialize or get the git repository
            FileRepositoryBuilder repositoryBuilder =
                    new FileRepositoryBuilder();
            Repository repository = repositoryBuilder.setGitDir(directory)
                    .setMustExist(true)
                    .build();
            Git git =  new Git(repository);

            //Get all commits
            Iterable<RevCommit> iterable = git.log().call();
            Iterator<RevCommit> iterator = iterable.iterator();
            List<RevCommit> commits = new ArrayList<>();
            while (iterator.hasNext())  {
                commits.add(iterator.next());
            }

            //Iterate through all pairs of consective commits
            if(commits.size() > 1)  {
                RevCommit master = commits.get(0);
                RevCommit commit1 = commits.get(0);
                for(int i = 1; i < commits.size(); i++) {
                    RevCommit commit2 = commits.get(i);
                    System.out.println();
                    System.out.println("[Commit1] = " + commit1.getFullMessage() + " " + commit1.getName());
                    System.out.println("[Commit2] = " + commit2.getFullMessage() + " " + commit2.getName());

                    //Build the tree iterators for the commits
                    AbstractTreeIterator oldTreeIterator;
                    AbstractTreeIterator newTreeIterator;

                    ObjectId treeId1 = commit1.getTree().getId();
                    ObjectId treeId2 = commit2.getTree().getId();

                    try(ObjectReader reader1 = git.getRepository().newObjectReader())    {
                        newTreeIterator = new CanonicalTreeParser(null, reader1, treeId1);
                    }

                    try(ObjectReader reader2 = git.getRepository().newObjectReader())    {
                        oldTreeIterator = new CanonicalTreeParser(null, reader2, treeId2);
                    }

                    //Get the list of differences
                    if(oldTreeIterator != null && newTreeIterator != null)  {
                        List<DiffEntry> diffs = git.diff()
                                .setOldTree(oldTreeIterator)
                                .setNewTree(newTreeIterator)
                                .call();
                        System.out.println("No of changes = " + diffs.size());
                        /*
                        for(DiffEntry diff: diffs)  {
                            File oldFile = new File(diff.getOldPath());
                            //Call change distiller here
                            //git.checkout().setName(commit2.getName()).call();
                            File newFile = new File(diff.getNewPath());
                            if(newFile != null && oldFile != null)  {
                                System.out.println(oldFile.getAbsolutePath());
                                System.out.println(newFile.getAbsolutePath());
                            }

                            System.out.println(diff.toString());
                        }
                        */
                    }
                    else {
                        System.out.println("Iterator is null");
                    }

                    //Slide window
                    commit1 = commit2;
                }
                //git.checkout().setName(master.getName()).call();
            }

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
