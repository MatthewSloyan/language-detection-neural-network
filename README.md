Below is an outline of the sections contained in this document and what they entail. 

* “Section 1” looks at the development process and all the research, attempts and improvements to the neural network along with my thoughts in chronological order. These topics include setup, activation functions used, nodes in hidden layer, n-gram size, hashing feature vector size, drop-out rate, error-rate, pre-processing, pruning, and adding more layers. 
* “Section 2” looks at the final implementation of the neural network (Topology, Confusion Matrix etc.) along with my thoughts on the research and findings from Section 1.
* “Section 3” describes the class structure, design patterns used, a user guide, extras implemented and testing results with live data.

### Tested On
Windows 10 PC (i7) & Ubuntu VM

### How to run
* If the Encog jar is on the classpath it can be run with the following command: `java –cp ./language-nn.jar ie.gmit.sw.Runner`
* If not then this command will work: `java -cp ./encog-core-3.4.jar;./language-nn.jar ie.gmit.sw.Runner`

Also please note the Wili dataset must be in the same directory as jar E.g. “./wili-2018-Small-11750-Edited.txt”

## SECTION 1
#### Initial setup
I setup the project by allowing me to easily change the variables such as vector size, ngram size etc. I began by processing the data and setting up basic neural network. This configuration had vector input size of 200, a Sigmoid input and output layer along with one Sigmoid hidden layer with 100 nodes. As it was the first attempt without any research or improvements the accuracy was strangely high and of course very fast due to the small number of inputs and nodes. 

So, I needed to look at accuracy properly. I did some research into what actually is output by “output.getData();”. From testing this is the values of all the possible 235 language weighted outputs summed from the previous layers. As I was using a Sigmoid activation function the values were variable and could sum up to a total of 5, 6 or 8. With this I realised I was calculating the correct values wrong, and instead I should be getting the index of the max value and comparing them for y and yd. Now with the same parameters as above I was getting %12 accuracy, which needed to be improved but it was a starting point to go from.

I decided to try and normalise my testing efforts so I could measure my improvements, so I tried to get the accuracy as high as possible with what I currently had and go from there. I also wanted the training time to be short for testing purposes. I started by increasing the input vectors to 500 from 200, and then to 750 which can be seen below. I was using ngrams of length 3 also. I later looked at the ngram and vector input size more in detail.

* Increased inputs 500, and hidden layer nodes to 250 - Accuracy 16% and 15 second training time.
* Increased inputs 750, and hidden layer nodes to 400 - Accuracy 30% and 40 second training time.

#### Activation Functions
With my accuracy very low, I decided to move to the layer types and fine tune them correctly. I started by researching all the different possible activation functions that could be used, their benefits and why they are used. Below is some of my thoughts on each and my tests with them.

**What are activation functions?**

They are mathematical equations that are applied to each neuron in the network. This can be seen below in the equation. The activation function determines if the neuron should be activated or not and helps normalize the output of each neuron to a range between 0 and 1 or between -1 and 1. The result is then fed to the next layer of neurons. Activations should be computationally efficient as they are applied thousands of times per iteration (epoch), and this is something that would be important when working with a large dataset and large number of nodes.

`Y = ActivationFunction((weight * input) + bias)`

There are a few different types of activations, these include binary step, linear and non-linear functions. It seemed however binary step functions would not be suitable as they do not allow multi-value outputs E.g. 235 languages. Again, linear functions would not be suitable either despite the fact they allow multiple outputs. Backpropagation is impossible as the derivative has no relationship with the input. Also, no matter how many layers you add they all collapses into one. This brought me onto non-linear functions which would provide the complexity required to train and learn this dataset [4].

**Softmax**
Softmax functions can handle multiple classes of outputs. All the outputs are normalized between 0 and 1 and then divided by their sum. This creates a proportional output that sums to 1. Because of this Softmax is highly recommended for the output layer [4], as multiple inputs can be classified into many categories E.g. Languages in this case.
I applied a Softmax output layer, which improved the accuracy overall, and made the outputs all sum to 1. Testing attempts can be seen in the list below all activation functions. 

**Sigmoid**
The first function I researched was Sigmoid. As the network was initially setup using Sigmoid layers, I felt it would be a good starting point. Sigmoid produces a S shaped sigmoidal curve which its inputs are converted to outputs in the range between 0 and 1. Sigmoid is a very computational heavy activation function but works well with classifications [4]. A problem that can occur with Sigmoid is the vanishing gradient problem. This occurs when working with very high or very low input values. Due to the curve sometimes there will be no change, which can cause the network to refuse to learn further and add to the training time. This is problematic when there are several hidden layers.

Due to its computational burden it might not be suitable for all layers but from testing it worked quite well on the input layer improving the overall accuracy, with a slight decrease to training time. This can be seen in the test attempts list below the activation functions. 

**ReLU (Rectified Linear Unit)**
From research it seems that ReLU is the most used activation function today especially in convolutional neural networks or deep learning. The range of output is from 0 to infinity. If the input is greater than 0, the input stays the same. If the input is negative the result is zero, which means this neuron does not get activated. This makes ReLU very computationally efficient compared to TanH and Sigmoid. The fact that all negative values are lost however causes the “The Dying ReLU problem”, which is solved using the Leaky ReLU function [5]. This attempt includes a small positive slope in the negative area. However, this function does not seem to be included in the Encog API [1].

As our dataset is normalised between 0 and 1 this activation might not be useful as the values would never be updated due to the fact they are always above 0. However, it is substantially more computationally efficient than Sigmoid. From testing I applied a combination of ReLU functions to both the input and hidden layer however there was a decrease in accuracy, but a decrease in training time too, as seen in the list below. Ultimately I decided not to use it.

**TanH/Hyperbolic Tangent**
TanH is very similar to Sigmoid, however it is zero centred, which is better for strong negative, neutral or positive values. Its output range is between -1 and 1. It is generally considered to be better than Sigmoid [4]. For this current dataset it would be very suitable for the inputs, as all values are normalised between 1 and 0. Within this input range the curve is quite steep and provides output values from 0 to 0.8. Based on this I began to try TanH on the input, and hidden layer to see if there was any improvement and what I found compared to ReLu is that the training time is slower, but if just applied to the hidden layer the accuracy was improved drastically. This was used in my final design.

**Various Configuration Attempts**
This is some of the attempts at configuring the neural network with the researched activation functions above. These attempts all contained 750 nodes on the input layer, 400 nodes on the hidden layer, and 235 on the output layer. The error rate was also capped at 0.0046 which was drastically improved later. Also 3 ngrams were used, and all values below are averages as tests were run multiple times.

* Sigmoid, Sigmoid, Sigmoid – 12% accuracy and 58 seconds to train.
* Sigmoid, Sigmoid, Softmax – 30% accuracy and 52 seconds to train.
* Sigmoid, ReLu, Softmax – 38% accuracy and 45 seconds to train.
* Relu, Relu, Softmax – 43% accuracy and 55 seconds to train.
* ReLu, TanH, Softmax – 64% accuracy and 37 seconds to train.
* TanH, TanH, Softmax – 63% accuracy and 42 seconds to train.
* TanH, Sigmoid, Softmax – 58% with 50 seconds to train.
* Sigmoid, TanH, Softmax – 66% with 38 seconds to train.

Based on this information I decided to use the Sigmoid, TanH and Softmax as it provided the quickest training time, the highest accuracy, and the lowest error after 3 epochs by far.

**Other attempts**
I also tried an endless number of other combinations of the some of the other activation functions supplied by Encog [1]. Some of these included BiopolarSteependedSignmoid, Gassian, SIN and Log however none seemed to improve upon the Sigmoid, TanH and Softmax configuration I had developed as shown in the previous list.

#### Number of nodes in Hidden layer
A neural network can contain any number of hidden layers with any number of number of nodes, however one hidden layer can compute most complex tasks. The number of nodes a hidden layer has can drastically affect the efficiency and accuracy of a neural network. If there are too many nodes it is called overfitting. This causes the nodes to not see enough training data and makes it harder to train and adjust their weights correctly. If there are too few nodes, then this is called underfitting. This will cause the nodes to get packed with training data and will not be able to correctly detect the signals. Unlike overfitting this will work well with the training data, but poor with the testing data. Based on this it would be important to get this right. 

With the accuracy consistent I would I wanted to be able to determine the hidden layer nodes dynamically and correctly according to predefined equations. This begs the question however, how many nodes should there be? I nice quote I found is by Jeff Heaton (Creator of Encog) and he said, 'the optimal size of the hidden layer is usually between the size of the input and size of the output layers' [2].
From watching the videos supplied on Moodle and my own research I tried the following equations. The accuracy did not increase much as I was already using a proportional ratio from testing, but this would allow any number of vector sizes to be input.

* `int hiddenLayerNodes = inputs + outputs;` - Improved accuracy, but too many nodes for this dataset.
* `int hiddenLayerNodes = (int) ((inputs * 0.66) + outputs);` - Brought training time up too much as too many nodes.
* Geometric Pyramid Rule (`int hiddenLayerNodes = (int) Math.sqrt(inputs * outputs);`)

This takes the inputs and outputs and gets the square root. The number of hidden layer nodes is quite proportional to the input. For 1000 vectors the number of hidden nodes is 484 which is between the number of outputs and inputs as Jeff Heaton said. 

* Upper bound with scaling factor (`int hiddenLayerNodes = (int) (11750 / (scalingFactor * (inputs + outputs)));`)

This worked quite well too with a scaling factor of 0.015, however this would grow exponentially if the inputs were higher. I also tried lower and higher scaling factors but again it can create either too many nodes or too little very easily.
Based on this I choose the Geometric Pyramid Rule (GRP) going forward, but the other successful methods are included in the code as they could be used for different purposes in the future. The GPR provided the most proportional ratio for dynamically determining how many hidden layer nodes to use.

#### Vector Input Size
An area that affects the accuracy and training time is the number of vector inputs. If there are too many the data might contain a lot of zeros (underfitted), but too little then it may be over fitted. I tested multiple inputs sizes and found there was a sort lose/win situation. For example, if you used a low number of inputs (300/400) the training time between each epoch would be quicker but ultimately it would take more epochs to get to the desired error rate. If a higher number such as 900/1000 was used, then the epochs would be slower but the number of epochs to get to the desired error rate is also lower. So, it balances itself out with the higher number taking slightly longer overall. The latter also always gets to a low error rate every time, however if a small number is used then the error rate may never be achieved and the training will timeout. Based on this I found the sweet spot for this configurated to be around 700 inputs nodes that would provide a training time of around 95 seconds and an accuracy of 85% and an error rate of 0.0015.
Significantly larger inputs such as 1000, 1500 and 2000 were also tried but the training time was too long and did not improve the accuracy much.

#### Kmers/ngrams
I also looked at was how many kmer/ngrams to use. If there were too much data, it could be overfitted or vice versa. Below you will find some of my testing efforts and the results.

* 4 – I was originally trying 4 ngrams/kmers in my original tests, and the results were pretty good, but I felt it is not enough to satisfy 750 vector inputs. This seem to cause a lot of inputs to be 0, which could be skewing the accuracy.
* 3 – This attempt drastically improved accuracy and was used in tests above (66%)
* 2 – I then tried 2 length ngrams, which improved the accuracy again to 75% on average.
* 3 & 4 – This attempt brought he accuracy down to 30%
* 2 & 3 - This attempt was a slight improvement on the above (3 & 4), but still not as good as 2.
* 2, 3 & 4 - This attempt was again a slight improvement from 3 & 4 and 2 & 3, but again lower than 2.

From the above tests I found that using Kmers of length two was the most accurate in terms of accuracy, so I have recommended this as the option to use in the UI and going forward.

#### Error Rate
From research and testing the error rate makes a big difference to the accuracy, if the error rate were 0.01 the accuracy would always seem to be around 10%. Below are several attempts to bring the error rate down. This is used when training as a limit to determine when to stop. 

0.01 – Initial starting point, this would usually complete the training very quickly in 1 epoch, however error rate would usually go lower than this to about 0.005 within 1 epoch.
0.005 - This limit would usually take around 1 epoch and increased the accuracy again by about 5%. 
0.0025 - This limit slightly increased the training time and the accuracy and would take 2 epochs.
0.0017 – This is a low as I could get it without the error rate starting to rise again on the next epoch. This is where the nodes could not train anymore. With 4 epochs it would now train in around 95 seconds with an error rate of 0.00168 and an accuracy of 75%.

#### Drop-out rate
The drop out rate is concept where the hidden layers have a fixed probability between 0 and 1 of being present during training. This can speed the training and lower some of the overfitting that can occur [6]. 

From research I found that Encog provides this functionality as a parameter to the constructor of the BasicLayer. I tested various rates and found that a high drop out rate of 0.8 on all layers decreased the training time by 15 seconds and increased the accuracy by 5%.

**Sigmoid, TanH, Softmax, 2 ngrams, 650 inputs, 390 HL nodes (GPR) with a 0.0025 error**
* 80% Accuracy took 79 seconds to train time with 0.98 dropout rate on HL.
* 80% Accuracy took 72 seconds to train time with 0.8 dropout rate on HL.
* 80% Accuracy took 80 seconds to train time with 0.8 dropout rate on IN and HL.
* 79% Accuracy took 80 seconds to train time with 0.3 dropout rate on HL.
* 81% Accuracy took 65 seconds to train time with 0.8 dropout rate on all layers.

#### Pre-processing
Some of the last steps I worked on was pre-processing and improving the training data to get the most accurate results. I first removed all special characters from the dataset, but that would remove the other languages too. I then removed the punctuation which improved accuracy by around 2%. Lastly, I removed all numbers as they define a specific language. The accuracy was now consistently at around 85% with 700 input vectors and an error rate of 0.0015.

#### Adding more hidden layers
Adding more layers adds to the training time and complexity exponentially, so therefore I left it to the end and until I could make no further improvements. I started by adding another hidden layer with a TanH activation function and the same number of notes determined by GPR. This however improved the training time by around a minute for an accuracy increase of 2% so the trade-off was not worth it. I tried a few other combinations of hidden layers and nodes, but I incurred the same problem. 

#### Other attempts
There were also some failed attempts at improving the accuracy, some research brought me onto Pruning which can be used to stimulate week neurons or remove neurons. By looking at the Encog Documentation I found that this was possible using “PruneSelective” [1]. I tried stimulating weak nodes, but the error rate was increased to 0.004. I decided to not use this, but I felt it could be useful to update the network and neuron count on the fly to improve the error rate.

Another attempt I made was to use BackPropagation instead of ResilientPropagation to train, however this increased the training time significantly.

## SECTION 2
This section covers the final implementation of the following topics in retrospect to the research and tests from “Section 1”. More information, reasons and tests for the below topics can be found in Section 1 under their respective headings.

#### Overall Topology Design & Activation functions 
Below is the final topology design I have chosen and why. All layers have a dropout rate of 0.8 which improved the training time by around 15 seconds and accuracy by 5%. When using 2 ngrams with 700 vector inputs the overall average accuracy is 85%.

* **Input Layer** – Sigmoid Activation function with x number of input nodes.
I have chosen Sigmoid as it produces a gradual sigmoidal curve between 0 and 1 which is what the dataset is normalised to. It also performed very well from testing, despite its computational burden. The number of input nodes is the size of the vector feature hash.

* **Hidden Layer** – TanH Activation function with x number of nodes calculated by using the Geometric Pyramid Rule (GPR)
I have chosen TanH for the hidden layer as for this dataset it would be very suitable for the inputs, as again all values are normalised between 0 and 1. Within this input range the curve is quite steep and provides output values from 0 to 0.8. Also, TanH provided the best accuracy and training times when on the hidden layer. 

* **Output Layer** – Softmax Activation function with 235 number of nodes.
I have chosen Softmax for the output layer as it normalizes the values, so they sum to 1. It is also recommended to be used for output layer which I found from research. Lastly, it increased the accuracy overall.

#### N-gram Size and hashing approach used
The N-gram size I found to be most fruitful was n-grams of length 2. I tried all possible combinations, but they would either fill the dataset too much or too little (A lot of zeros). The accuracy of these attempts can be found in Section 1. For each line of the Wili dataset the string is first pre-processed to remove punctuation and numbers. It is then parsed into ngrams of length 2 and hashed into a vector with a specified length. The formula used to hash the vectors is `kmer.hashCode() % vector.length`, and allows any number of words or ngrams to be hashed into one vector. This vector is then normalised and written to the csv file along with the correct language. This approach can eliminate the “curse of dimensionality”.

#### Hashing feature vector size
An area that affects the accuracy and training time is the number of vector inputs. If there are too many the data might contain a lot of zeros (underfitted), but too little then it may be over fitted. I tested multiple inputs sizes and found there was a sort lose/win situation. For example, if you used a low number of inputs (300/400) the training time between each epoch would be quicker but ultimately it would take more epochs to get to the desired error rate. If a higher number such as 900/1000 was used, then the epochs would be slower but the number of epochs to get to the desired error rate is also lower. So, it balances itself out with the higher number taking slightly longer overall. The latter also always gets to a low error rate every time, however if a small number is used then the error rate may never be achieved if too low and the training will timeout. Based on this I found the sweet spot for this configurated to be around 700 inputs nodes that would provide a training time of around 95 seconds and an accuracy of 85% and an error rate of 0.0015. When trying extremely large values over 1000 the vector would be too sparse and take longer to train. 

#### Number of nodes in the hidden layer
Using the Geometric Pyramid Rule (GPR) to dynamically determine the number of nodes was the most proportional to all input sizes and worked well from testing. No matter the input size the correct estimation required would be provided. This rule uses the following formula - hiddenLayerNodes = (int) Math.sqrt(inputs * outputs). For example, if 650 vectors inputs are used the number of hidden nodes is 390 which is between the number of outputs and inputs as Jeff Heaton recommended [2].

#### Sensitivity & Specificity (Confusion matrix)
I spent some time learning about the possible ways’ accuracy could be calculated. In this build I have three measures. First is the using the number of correct values and the total ((correct / total) * 100). The second is using a confusion matrix with TP, TN, FP, and FN. With this information I have calculated the Sensitivity (TP * (TP + FN)) and Specificity (TN / (TN + FP)), along with the accuracy which is consistently at 0.99. I am not sure if I have fully calculated the TP, TN, FP, and FN correctly, but the results are consistent and proportional. Lastly, I have calculated Matthew's Correlation Coefficient using the formula provided [3] which returned a result of 0.85. If the result is -1 then the binary classifier is wrong, but if it +1 it is completely correct. As the result is close to +1 it is considered good.

#### Conclusion & Thoughts
Overall, I am extremely happy with the outcome of this project. It provides consistently accurate results in a reasonable time (85% accuracy in 95 seconds). With more training time and configurations attempts the error rate could be lowered slightly but maybe a larger dataset would be required. I am also very happy with the knowledge I’ve learned and I feel I have a deeper grasp of what’s going on under the hood and the efforts that are required to fine tune a network for a specific purpose. Lastly, this knowledge will help me greatly in my career as a machine learning engineer.

## Section 3
Throughout the process I made various decisions from brainstorming, writing it out on paper and trial and error to get the most accurate result efficiently. I also used the lecture notes and labs provided for guidance regarding design principles and patterns, loose-coupling, high cohesion, abstraction, encapsulation, and composition. Some of the design decisions and a guide for each feature can be found below under their relevant headings.

### Quick User Guide
When the main menu is loaded the user can create a new neural network, load an existing network, or view recommendations. 

* Creating a neural network – The user will be given the option to create a new 5-fold cross validation neural network. The ngrams and vector feature size can be input which is fully validated. This starts the neural network training, which is capped at an error rate of 0.0017, 5 epochs or 3 mins of training. Once completed the training time is displayed with the error rate. Then the user is shown a multitude of options which include “View topology, run tests, save model, predict with file and predict with string.” 
* Loading a neural network – The user will be given the option to load a neural network model file. The ngrams and vector feature size can be input which is fully validated and must be the same values used when creating the model. Once loaded the user can predict a language with a file or predict with string. 

### Classes
#### CrossValidationNeuralNetwork
Main class that contains all functions for training the neural network. A factory design pattern is used with this class, so that the network type can be swapped out easily. Networkable is implemented, so that any number of neural network types could be created. The network is configured using the configurations designed to provide the most accurate and efficient result, this can be seen in Section 1 & 2. The network is also trained using 5-fold cross validation. A limit is placed on the training that it stops when an error rate of 0.0017, 5 epochs or 3 minutes have passed. These values were determined from testing to provide the best results.

#### NeuralNetworkFunctions
NeuralNetworkFunctions holds all the common functionality used for this class such as testing, predicting, and viewing the topology. I abstracted these functions from CrossValidationNeuralNetwork so more network types could easily be added and use the same code as Networkable is passed into the constructor.

#### NeuralNetworkFactory & NeuralNetworkable
Factory design pattern used to create neural network type. In this current build there is only support for 5-fold cross validation, but more could easily be added. This avoids tight coupling between the creator and the concrete networks. A Singleton design pattern is also used for Factory instance as there should only be one. The interface NeuralNetworkable only contains functionality specific to a neural network type. E.g. its configuration and training. Common functionality such as testing, and prediction is abstracted to NeuralNetworkFunctions.

#### TrainingProcessor & TestProcessor
These two classes are used to process the Wili dataset and user strings or files into a vector feature hash. These classes have code in common, but I felt it necessary to have one each for simplicity and modification. Again, an interface is used so that more processor types can be added. TrainingProcessor processes the Wili dataset and writes it to a CSV file for training. TestProcessor processing user strings and files for testing with live data.

#### CreateMenu & LoadMenu
Menu classes that delegate all work to UIFunctions, NeuralNetworkFunctions and neural network types (CrossValidationNeuralNetwork). These act as the hub for all processes of the application.

#### UIFunctions
All UI input functions are included in this class and are called from CreateMenu & LoadMenu. Functions such as getting the number of n-grams or vector size from the user, the file to predict, the string to predict, the name to save the neural network and how to load the neural network are included. I have abstracted this functionality out as it is common to both menus and is reusable. All methods use the NeuralNetworkable interface or NeuralNetworkFunctions as parameters so that any type can be passed in to promote reusability.  It also holds the user data which is used to setup processing and neural network types.

### Design Patterns and Principles
#### Factory
I have decided to implement a factory design pattern used to create neural network types. This avoids tight coupling between the creator and concreate types. Also upholds the Single Responsibility Principle (SRP). You can move the creation code into one place in the program, making the code easier to support. The Open/Closed Principle (OCP) is also upheld, as new types of networks can be introduced into the program without breaking existing code.

#### Singleton
I have implemented a Singleton pattern in the NeuralNetworkFactory as there should only be one instance of it. 

#### Single Responsibility Principle (SRP)
At all levels throughout the design SRP is upheld as I tried to give every class a specific purpose that delegates to other classes when required.

#### Open Close Principle (OCP)
Seen in the factory implemented. You can introduce new neural network types without changing any of the code as they all use the NeuralNetworkable interface. 

#### Dependency Inversion Principle
No higher-level module depends on a low-level module. Both interfaces implemented are by classes at the same level and below the interface.

#### Interface Segregation Principle
No class implements interface methods that it does not use. For example, each Neural network type just has a startTraining method and then all other functions are handled by the same NeuralNetworkFunctions class which is common to any type.

#### Law of Demeter
No single function knows the whole navigation structure of the system. I have tried to subjugate all functionality into multiple methods. 

### Sample Tests 
I tested various live sets of data with various languages. The testing averaged around 85% accuracy so I found that the live results could be quite good especially with some languages that are unique. For some languages like Spanish however it would sometimes predict Chavacano, Aragonese etc but this makes sense as these are areas in Spain and use much the same language. The same can be said for other languages too. Below is some sample text I used to produce good results.

* Spanish - Hola! Yo empecé aprendo Español hace dos mes en la escuela. Yo voy la universidad. Yo tratar estudioso Español tres hora todos los días para que yo saco mejor rápido. ¿Cosa algún yo debo hacer además construir mí vocabulario? Muchas veces yo estudioso la palabras solo para que yo construir mí voabulario rápido.

* Irish - Dia Duit! Conas atá tú? Is breá liom bheith ag obair le madraí agus cait. Go raibh maith agat as labhairt liom. Is maith liom freisin féachaint ar scannáin agus ar chluichí le cairde chomh maith le bheith ag éisteacht le ceol ag an deireadh seachtaine.

* French - Le lundi matin, mon père travaille au bureau, ma mère reste à la maison, ma petite sœur va à l'école, et je vais à l'université. Le mardi, le mercredi, le jeudi, et finalement le vendredi, nous faisons la même chose. Mais le week-end, il est assez différent. Pendant le week-end, nous ne sommes pas très occupés comme les autres jours. Le samedi matin, mon père qui est très sportif fait de la natation, et ma mère fait la cuisine parce que chaque samedi, mes parents invitent ma tante à dîner avec nous. Enfin, le dimanche d'habitude nous ne faisons pas grand-chose; quelques fois, mon père fait du bricolage si nécessaire.

### Extras (All documented above)
* Ability to predict a language from a string along with reading from a file.
* Ability to load in a saved model to predict language with.
* All UI inputs are validated E.g. It only allows numbers within a certain range and will display error and allow the user to try again if invalid numbers or strings are input. Files are also validated and checked if they exist.
* Added multiple methods to calculate hidden layer size.
* Full Confusion Matrix implemented.
* Three accuracy measures (Correct/total, Confusion Matrix, and Matthew's Correlation Coefficient)
* Commented to JavaDoc standard.
* Design patterns such as Factory and a Singleton implemented to improved overall design and reusability.

## References
* [1] - Encog Activation Functions (Java Docs) - http://heatonresearch-site.s3-website-us-east-1.amazonaws.com/javadoc/encog-3.3/org/encog/engine/network/activation/package-summary.html
* [2] – Jeff Heaton Research Books - https://www.heatonresearch.com/book/
* [3] - Multi-Layer Neural Networks - https://web.microsoftstream.com/video/25fa319e-1f44-47ae-8b07-44d892c3463d
* [4] - Karlik, B. and Olgac, A.V., 2011. Performance analysis of various activation functions in generalized MLP architectures of neural networks. International Journal of Artificial Intelligence and Expert Systems, 1(4), pp.111-122.
* [5] – Activation Functions - https://medium.com/the-theory-of-everything/understanding-activation-functions-in-neural-networks-9491262884e0
* [6] – Dropout Rate - https://machinelearningmastery.com/dropout-for-regularizing-deep-neural-networks/

