## Example

### Parameters

<table class="table">
  <thead>
    <tr>
      <th style="width:20%">Name</th>
      <th style="width:80%">Value</th>
    </tr>
  </thead>
  <tbody>
  <tr>
    <td><code>input column</code></td>
    <td>"features"</td>
  </tr>
  <tr>
    <td><code>output</code></td>
    <td>append new column</td>
  </tr>
  <tr>
    <td><code>output column</code></td>
    <td>"pca_features"</td>
  </tr>
  <tr>
    <td><code>k</code></td>
    <td>3.0</td>
  </tr>
  </tbody>
</table>

### Input

<table class="table">
  <thead>
    <tr>
      <th>features</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>[0.0,1.0,0.0,7.0,0.0]</td>
    </tr>
    <tr>
      <td>[2.0,0.0,3.0,4.0,5.0]</td>
    </tr>
    <tr>
      <td>[4.0,0.0,0.0,6.0,7.0]</td>
    </tr>
  </tbody>
</table>

### Output

<table class="table">
  <thead>
    <tr>
      <th>features</th>
      <th>pca_features</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>[0.0,1.0,0.0,7.0,0.0]</td>
      <td>[1.6485728230883814,-4.0132827005162985,-1.0091435193998501]</td>
    </tr>
    <tr>
      <td>[2.0,0.0,3.0,4.0,5.0]</td>
      <td>[-4.645104331781533,-1.1167972663619048,-1.0091435193998504]</td>
    </tr>
    <tr>
      <td>[4.0,0.0,0.0,6.0,7.0]</td>
      <td>[-6.428880535676488,-5.337951427775359,-1.0091435193998508]</td>
    </tr>
  </tbody>
</table>

