package com.tcay.slalom

import com.tcay.util.DuplicateBibException
import org.testng.Assert
import org.testng.annotations.Test

import javax.swing.Icon
import javax.swing.ImageIcon

/**
 * Created with IntelliJ IDEA.
 * User: allen
 * Date: 11/3/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
class RaceTest {

    Race race;
    void setUp() {
        race = new Race()
    }

    void tearDown() {
        race = null
    }



    @Test(groups = ["unit"])
    void testGetInstance() {
        Race ourRace = Race.getInstance()
        Assert.assertNotNull(ourRace)
    }

    @Test(groups = ["integration"])
    void testGetTagHeuerConnected() {

    }

    @Test(groups = ["integration"])
    void testSetTagHeuerConnected() {

    }

    @Test(groups = ["integration"])
    void testGetPhotoCellThread() {

    }

    @Test(groups = ["integration"])
    void testSetPhotoCellThread() {

    }

    @Test(groups = ["integration"])
    void testUpdateSectionOnline() {

    }

    @Test(groups = ["integration"])
    void testStartTagHeuerListener() {

    }

    void testSetUpstreamGates() {

    }

    void testGetTempResults() {

    }

    void testGetCurrentRunIteration() {

    }

    void testGetRunTitle() {

    }

    void testGetPendingRerun() {

    }

    void testSetPendingRerun() {

    }

    void testGetCurrentRunIterationOrdinal() {

    }

    @Test(groups=["unit"])
    void testClearRace() {
        ArrayList<RaceRun> emptyList = new ArrayList<RaceRun>()

        Race race = Race.getInstance();
        race.clearRace();

        Assert.assertEquals(race.getActiveRuns(), emptyList)
        Assert.assertEquals(race.getCompletedRuns(),emptyList)
        Assert.assertEquals(race.getCompletedRunsByClassTime(), emptyList)
        Assert.assertEquals(race.getCurrentRunIteration(), 1)
        Assert.assertEquals(race.getCurrentRunIterationOrdinal(), Race.FIRST);
        Assert.assertEquals(race.getUpstreamGates(),new ArrayList<Integer>() );

        Assert.assertEquals(race.getLocation(),"")
        Assert.assertEquals(race.getName(), Race.UNNAMED_RACE)
        Assert.assertEquals(race.getNbrGates(), Race.DEFAULT_NBR_GATES)
        Assert.assertEquals(race.getNbrOfSections(), 0)
        Assert.assertEquals(race.getRacers(), new ArrayList<Racer>())
        Assert.assertNull(race.getPendingRerun())
        Assert.assertEquals(race.getRemainingStartList(),new ArrayList<BoatEntry>())
        Assert.assertEquals(race.getRunsStartedOrCompletedCnt(), 0)
        Assert.assertEquals(race.getScorableRuns(), emptyList)
        Assert.assertEquals(race.getSectionsConnectedNamesAsString(),Race.SECTIONS_NONE)
    }

    void testRemoveCompletedRun() {

    }

    void testIncrementCurrentRunIteration() {

    }

    void testSetName() {

    }

    void testSetLocation() {

    }

    void testGetNbrGates() {

    }

    void testGetSections() {

    }

    void testGetUpstreamGates() {

    }

    void testIsUpstream() {

    }

    void testGetNbrOfSections() {

    }

    void testGetSectionEndingGates() {

    }

    void testIsFirstGateInSection() {

    }

    void testGetSectionsConnectedNamesAsString() {

    }

    void testGetSection() {

    }

    void testIsGateInSection() {

    }

    void testIsLastGateInSection() {

    }

    void testGetCompletedRuns() {

    }

    void testGetExistingRun() {

    }

    void testGetCompletedRunsByClassTime() {

    }

    void testGetActiveRuns() {

    }

    void testGetRacers() {

    }

    void testAddBoat() {

    }

    @Test(expectedExceptions = DuplicateBibException.class, groups = ["regression", "integration"] )
    void testAddBoatAddDuplicateBibInAClass() {
        race = Race.getInstance()
        System.out.println("Clearing race data");
        race.clearRace()
        TestData testData = new TestData()
        testData.load()
        System.out.println("Loading test race data");

        // Get the first entry
        BoatEntry be = (BoatEntry)race.getRemainingStartList().get(0);
        System.out.println("Attempting to add the first boat as a duplicate");
        // Attempting to add this should throw an expected DuplicateBibException exception
        race.addBoat(be.getRacer(),be.boatClass);
    }




    void testAddRun() {

    }

    void testFinishedRun() {

    }

    void testGetRunsStartedOrCompletedCnt() {

    }

    void testGetScorableRuns() {

    }

    void testGetRemainingStartList() {

    }

    void testSaveSerializedData() {

    }

    @Test(groups=["integration"])
    void testLoadSerializedData() {
// todo race.getSectionEndingGates()
// todo        Assert.assertEquals(race.getRunTitle(), "")

        // fixme screwed up in Race -> getSection Assert.assertNull(race.getSection(1))
        //ArrayList<RaceRun> emptyList = new ArrayList<RaceRun>()

        Race race = Race.getInstance();
        race.clearRace();

        TestData testData = new TestData()
        testData.load()

        Assert.assertEquals(race.getNbrGates(), TestData.NBR_GATES)
        Assert.assertEquals(race.getLocation(), TestData.LOCATION)
        Assert.assertEquals(race.getName(), TestData.RACE_NAME)
        Assert.assertEquals(race.getUpstreamGates(), TestData.UPSTREAMS)



    }

    void testFindRun() {

    }

    void testSaveTagHeuer() {

    }

    void testUpdateResults() {

    }

    @Test(groups = ["unit"])
    void testGetImageIcon() {
        Race race = Race.getInstance()
        Icon icon = race.getImageIcon("USA")
        Assert.assertNotNull(icon)

        icon = race.getImageIcon("FRA")
        Assert.assertNotNull(icon)

        icon = race.getImageIcon("SVK")
        Assert.assertNotNull(icon)

    }

    @Test(groups = ["unit"])
    void testGetImageIconTiny() {
        Race race = Race.getInstance()
        Icon icon = race.getImageIconTiny("USA")
        Assert.assertNotNull(icon)

        icon = race.getImageIconTiny("FRA")
        Assert.assertNotNull(icon)

        icon = race.getImageIconTiny("SVK")
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetStopWatchII() {
        Race race = Race.getInstance()
        Icon icon = race.getStopWatchII();
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetDownstreamTinyII() {
        Race race = Race.getInstance()
        Icon icon = race.getDownstreamTinyII()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetUpstreamTinyII() {
        Race race = Race.getInstance()
        Icon icon = race.getUpstreamTinyII()
        Assert.assertNotNull(icon)
    }


    @Test(groups = ["unit"])
    void testGetUpstreamSmallII() {
        Race race = Race.getInstance()
        Icon icon = race.getUpstreamSmallII();
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetDownstreamSmallII() {
        Race race = Race.getInstance()
        Icon icon = race.getDownstreamSmallII();
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetTagHeuerTinyII() {
        Race race = Race.getInstance()
        Icon icon = race.getTagHeuerTinyII();
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetTagHeuerII() {
        Race race = Race.getInstance()
        Icon icon = race.getTagHeuerII();
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetSlalomCourseSmall() {
        Race race = Race.getInstance()
        Icon icon = race.getSlalomCourseSmall()
        Assert.assertNotNull(icon)
    }




    @Test(groups = ["unit"])
    void testGetIcfPenaltyDescripton() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyDescripton()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetIcfPenaltyDiagram() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyDiagram()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetSlalomBackgroundII() {
        Race race = Race.getInstance()
        Icon icon = race.getSlalomBackgroundII()
        Assert.assertNotNull(icon)
    }



    @Test(groups = ["unit"])
    void testGetIcfPenaltyWrongDIrection() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyWrongDirection()
        Assert.assertNotNull(icon)
    }


    @Test(groups = ["unit"])
    void testIcfPenaltyWashedBackThroughGateLine() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyWashedBackThroughGateLine()
        Assert.assertNotNull(icon)
    }


    @Test(groups = ["unit"])
    void testGetIcfPenaltyUpsideDown() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyUpsideDown()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetIcfPenaltyIntentionallyMovedGate() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyIntentionallyMovedGate()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetIcfPenaltyHeadAndBoatNotInGateTogether() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyHeadAndBoatNotInGateTogether()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetIcfPenaltyDidntGoThroughas1Unit() {
        Race race = Race.getInstance()
        Icon icon = race.getIcfPenaltyDidntGoThroughas1Unit()
        Assert.assertNotNull(icon)
    }

    @Test(groups = ["unit"])
    void testGetKayakSmall() {
        Race race = Race.getInstance()
        Icon icon = race.getKayakSmall()
        Assert.assertNotNull(icon)
    }


    @Test(groups = ["unit"])
    void testGetRacerImg() {
        Race race = Race.getInstance()
        Icon icon = race.getRacerImg()
        Assert.assertNotNull(icon)
    }


    @Test(groups = ["unit"])
    void testGetRaceBibSmall() {
        Race race = Race.getInstance()
        Icon icon = race.getRaceBibSmall()
        Assert.assertNotNull(icon)
    }






}