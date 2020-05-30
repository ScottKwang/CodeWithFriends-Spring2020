# Torb: The ultimate home security system™
Created by [Timothy Cole](https://github.com/timothycole) and [Jack Douglas](https://github.com/jack-douglas)

![](https://cdn.tcole.me/cwf/IMG_2232.jpeg)

_© All code snippets in this post are covered under [MIT License](https://raw.githubusercontent.com/TimothyCole/timcole.me/master/LICENSE)_

---

### The idea
Last month we had someone hop our front gate, go into our garage, and try to steal our house mate [twomad](https://www.youtube.com/channel/UCGGdOIkVIZipux13jt7rprw/videos)'s electric bike.  
Lucky both Jack and I had an eye on the cameras at the time to notice the events unfolding and run outside and stop him.  
Already being a fans of airsoft we instantly got the idea of making an automatic turret for our project.  We had to keep a few things in mind though.  One, we don't want anyone to get seriously hurt and Two, we wanted it to be a fun project that we can actually have fun with instead of actually shootting at intruders. Although on our property without permission we still don't feel it's appropriate to shoot an un-consenting target.  
Keeping all this in mind we started planning. We knew we needed a few parts to do this, so we started looking.
<details>
  <summary>CCTV Footage</summary>
  <a href="https://youtu.be/kvpBa91Fu1A?t=716" target="_blank">
    <img src="https://external-content.duckduckgo.com/iu/?u=https://pbs.twimg.com/media/EWjsQ2NUwAAgEMb?format=png" />
  </a>
</details>

### The shopping list
We quickly realized that our biggest issue here was going to be covid, between delayed shipping and local shops being closed, getting the needed parts on time wasn't going to be easy.  We ended up using Amazon for most things, though it said shipping was going to take 3 weeks it all came fairly quickly, in about 1 week. We couldn't get aluminum extrusion on Amazon though, so we had to use a 3d printer part company we found SPOOL3D which got here in 3 days.
|Part|Store|Price (USD)|
|----|-----|-----|
|2040 Aluminum Extrusion 250mm|SPOOL3D|$29|
|2040 Aluminum Extrusion 500mm|SPOOL3D|$23|
|Power Supply|[Amazon](https://www.amazon.ca/gp/product/B00MAC9MO6/ref=ppx_od_dt_b_asin_title_s01?ie=UTF8&psc=1)|$25|
|Aluminum Nuts|[Amazon](https://www.amazon.ca/gp/product/B078GKCK8W/ref=ppx_od_dt_b_asin_title_s01?ie=UTF8&psc=1)|$26|
|Motors w/ Driver Board <sup>x2</sup>|[Amazon](https://www.amazon.ca/gp/product/B07FK8NRKL/ref=ppx_od_dt_b_asin_title_s00?ie=UTF8&psc=1)|$29 <sup>x2</sup>|
|Gun 1|[Amazon](https://www.amazon.ca/gp/product/B002DCFUXU/ref=ppx_od_dt_b_asin_title_s01?ie=UTF8&psc=1)|$17|
|Gun 2|Local Airsoft Shop|$40|
|||$218 <sup>tax+shipping</sup>|
We also did use parts that we already had including, an old Raspberry Pi, Webcam, Breadboard, and Cables.  
Context on why we needed 2 guns in section [Hacking the gun](#hacking-the-gun).

### Hacking the gun
The idea here is removing the electric trigger and attaching wires so we can bridge the connection via the raspberry pi. Before we  began we tested the gun by adding batteries to the gun, we quickly learned that this gun had a feeding issue where the plastic bb's kept getting stuck, we made a note of the issue so we knew we may need to mod it later. We started opening the guns enclosure so we could remove the trigger and solder on cable extensions where the trigger was. After being inside we discovered that it's very possible to just remove the need to a trigger all together, we wired up cables directly to the motor instead with the trigger switch set to always on. This way we're able to pulse power to the motor to fire the gun instead of using batteries and bridging the trigger connection.

We attached the new leads from our gun to a basic step-down voltage regulator which is connected to our power supply to make sure the gun works. We used a step-down voltage regulator because the power supply outputs 12v and we didn't want to fry the motor in the gun, so we matched it to the voltage of the batteries. Flipping the power on and magic, the gun motors spin up as expected!

Shortly following as were putting bb's in the gun to test fire, we found that since the bb's were getting stuck in the gun it was blocking the gears from moving making the motor just spin inside of the gear making the gun useless. We opened the gun backup, and applied super glue between the motor stem and the gear to no prevail, powering it back on after and the glue just broke loose.

Concerned it wasn't going to work, we started looking for a new gun, after a few hours of looking we finally found one at a local airsoft store here in Vancouver and decided to get it. It kept two the same principles, It was plastic making it very light, and was fully electronic.

The key differences between the guns is the new one uses a standard airsoft gun battery with a mini tamiya connector, this is nice because it allow us to use a mini tamiya female connector and send power to the gun without even needing to open it if we zip-tie the trigger down. Which is exactly what we did!

![](https://cdn.tcole.me/cwf/IMG_2229.jpeg)

For the most part this method worked as expected with only a few caveats. Connecting the mini tamiya connector to the step-down voltage regulator worked™ but the way the gun operates if you stop the gun in the middle of a fire cycle the gun stops with the springs in a pulled back position causing it to use to many amps to fire. Trying to pull the trigger in this state drops the regulator to a low voltage amount not being able to power the gun. This caused because the regulator we were using only supplied 1.5amps which wasn't enough to get the springs out of this already pulled back state.

To solve this issue what we ended up doing is risking the motor. The battery that is supposed to power the gun is a standard 4.8v battery, we decided to solve this amps issue we would just bypass the regulator all together and directly wire it to a relay connected direct to the power supply. This means when we trigger the relay we will be supplying the gun with a full 12v of power. Being almost 3x the needed/recommended power we did worry about frying the motor instantly, but this method worked flawlessly. Minus the fact it will inevitably degrade the motors lifespan and it makes the motor spin way faster, making the gun more powerful. Being more powerful isn't completely a bad thing though as it will allow us to shoot further than advertised, but still not hurt anymore than a pinch or a flick.

Connecting the leads from the relay to the GPIO on the raspberry pi we're able to trigger the gun programmatically! (As well as a hard button on the breadboard for testing)

<a href="https://cdn.tcole.me/cwf/LQ_IMG_2235.mov" target="_blank">![](https://cdn.tcole.me/cwf/shoot.png)</a>

### Building the frame
The frame for the turret is fairly basic, though we did change it from our original designs.

Our original design was more of a gimbal like design but we decided against it because it would cause added weight and harder to mount motors as we didn't have a drill bit exactly the size of the stepper motor stems.

We ended up going with a pan tilt design instead. This means that the gun will be offset from the center causing no proper 0° point.
This isn't really an issue though as we can just keep track of the guns offset and calculate the 0° point accordingly. A little super glue in the holes and we have a snug fit. No issues with this route! 😃

### Manning the turret
The turret is designed to be automatically controlled. That means we need a way to find our target and tell the motors where to point and shoot.

For this we used a webcam connected to my MacBook via USB. We thought about using AI to detect either movement or people but decided that wouldn't be smart as it could target un-consenting targets like Uber Eats driver, Wildlife, Moving Trees, Cars, etc. Instead we opted for targeting colours within an HSL range. We decided to go with HSL instead of RGB because HSL is more of what the human eye sees. Using the hue we're able to more accurately specify a specific colour and get similar shades. Imagine you have two objects, they're both red but one is dark one is light. With RGB you're not able to capture it as one colour as RGB is built up of specific red, green, blue mixes (hence the name). HSL is build on a hue spectrum, so I'm able to target specific ranges and get a human-like equivalence of "that is red".

Getting started we just decided to use HTML and JS to make things quick and easy based on our current knowledge.

Firstly, we need to get the pixel values of our camera frames. We're able to do this by painting the video frames from our camera to a canvas. Now that we have a canvas with our pixel data we can start to analyze the frames. Frame data for canvas' come back in RGBA so we need to convert the RGB part (ignoring the alpha since it will always be 255) to HSL.

```typescript
export const RGB2HSL = (r: number, g: number, b: number): number[] => {
  r /= 255;
  g /= 255;
  b /= 255;

  let cmin = Math.min(r, g, b),
    cmax = Math.max(r, g, b),
    delta = cmax - cmin,
    h = 0,
    s = 0,
    l = 0;

  if (delta == 0) h = 0;
  else if (cmax == r) h = ((g - b) / delta) % 6;
  else if (cmax == g) h = (b - r) / delta + 2;
  else h = (r - g) / delta + 4;

  h = Math.round(h * 60);
  if (h < 0) h += 360;

  l = (cmax + cmin) / 2;
  s = delta == 0 ? 0 : delta / (1 - Math.abs(2 * l - 1));
  s = +(s * 100).toFixed(1);
  l = +(l * 100).toFixed(1);

  return [h, s, l];
};
```

Now that we have our HSL values we're able to check if they're in our supplied ranges. Ranges consist of using a lower and higher boundary, and insure that our input lies between.

To run this check we're able to create a basic function that runs for every item in the current HSL checking if it's lower or equal to the higher boundary and higher or equal to the lower boundary.

```typescript
export interface HSL {
  h: number;
  s: number;
  l: number;
}

export const inRange = (current: HSL, low: HSL, high: HSL): boolean =>
  Object.entries(current).filter(([key, value]) => {
    if (low[key] >= value && value <= high[key]) return key;
  }).length === 0;
```

If all items pass the boundary test then we're good to go and we have our colour.

Keeping track of all the pixels positions we're able to make a map and the group pixels (allowing a 20 pixel gap for errors) to find our target. We're only able to target one thing at a time so the largest pixel group is used as our target. Now knowing our target, we get the center point of the group, calculate the pixels from the top and left of the canvas, giving us our x and y values to aim at.

![](https://cdn.tcole.me/cwf/2020-05-29%2002_57_56.gif)

With the colour target detection completed we're now ready to control our turret! All the stepper motors attached, everything put together it's now time to right driver code. The idea here is to have the web page ping an API running on the raspberry pi with the coordinates and a shoot command; simple enough.

Already being able to shoot programmatically from the [Hacking the gun](#hacking-the-gun) step. We focused on using the TB6600 driver board documentation in order to be able to make the motors move. By alternating the output on the connected GPIO pins we're able to make the motors move how we want. Hours of calculating the proper offsets later we're finally ready to test it!

There just one issue.. It's all to heavy..

![](https://cdn.tcole.me/cwf/IMG_2234.2020-05-29%2022_34_01.gif)

As the motors move into position the momentum from moving creates force more than the motors can handle causing them to just let go, making the motors just move freely.

To solve this issue we have two solutions. First being, since it doesn't happen all the time, adding sensors to detect it's heading will allow for it to correct itself when it does happen. Second option is getting more powerful motors to handle the weight of everything. Both are really great options and would love to actually do both! So we started hunting down the extra parts, With covid among us we determined it wasn't possible with the current options to get the parts in time for the end of May 😭.

### Conclusion
We had many troubles along the way that we were able to solve fairly quickly. Doing a project like this again we now know important details learned along the way to help streamline things. Even though we don't have a fully working product at the end; we don't consider this a lose and won't be accepting defeat. We're going to still get the parts needed to fix the weight problem and will get this working for some great getting shot with the boys fun™.

### Honorable mentions
[twomad](https://twitter.com/ModestTim/status/1258993187294572546) for almost getting his bike stolen and being the best "helping hand" 🤣
